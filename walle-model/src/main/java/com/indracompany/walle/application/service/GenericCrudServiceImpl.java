package com.indracompany.walle.application.service;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.ConstraintViolation;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.internal.guava.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import com.indracompany.walle.application.service.exception.AplicacaoException;
import com.indracompany.walle.application.service.exception.AplicacaoExceptionValue;
import com.indracompany.walle.application.service.exception.ValidacaoCampos;
import com.indracompany.walle.application.service.exception.WalleValidacoes;
import com.indracompany.walle.domain.shared.GenericEntity;
import com.indracompany.walle.infrastructure.persistence.hibernate.repository.GenericCrudRepository;
import com.indracompany.walle.infrastructure.util.BeanUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author eder
 *
 * @param <T>
 */
@Slf4j
public abstract class GenericCrudServiceImpl<T extends GenericEntity<I>, I, R extends GenericCrudRepository<T, I>> {

    @Autowired
    protected R repository;


    @Transactional(rollbackFor = Throwable.class)
    public T salvarEAtualizar(T entidade) {
        entidade.setPersisted(existe(entidade.getId()));
        ajustar(entidade);
        validar(entidade);
        entidade = repository.save(entidade);
        return entidade;

    }
    
    @Transactional
    public T salvar(T entidade) {
        ajustar(entidade);
        validar(entidade);
        return getRepository().save(entidade);
    }
    
    @Transactional
    public T atualizar(I id, T entity) {
        T savedEntity = buscar(id);
        BeanUtils.copyProperties(entity, savedEntity);

        return getRepository().save(savedEntity);
    }

    public Long contar() {
        return repository.count();
    }

    public List<T> listar() {
        return Lists.newArrayList(repository.findAll());
    }

    @Transactional(readOnly = true)
    public Page<T> listar(Specification<T> specification, Pageable pageable) {
        return repository.findAll(specification, pageable);
    }

    public T buscar(I id) {
        Optional<T> retorno = repository.findById(id);
        if (retorno.isPresent()) {
            return retorno.get();
        }
        return null;
    }

    public boolean existe(I id) {
        return !Objects.isNull(id) && repository.existsById(id);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void remover(T entidade) {
        try {
            repository.delete(entidade);
        } catch (Exception e) {
            throw new AplicacaoException(WalleValidacoes.ERRO_EXCLUSAO_GENERICO, e);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void remover(I id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new AplicacaoException(WalleValidacoes.ERRO_EXCLUSAO_GENERICO, e);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void removerTodos() {
        repository.deleteAll();
    }

    private void ajustar(T entidade) {
        if (entidade == null) {
            return;
        }

        try {
            Field[] campos = entidade.getClass().getDeclaredFields();

            for (Field f : campos) {
                f.setAccessible(true);
                Object object = f.get(entidade);
                if (object instanceof CharSequence) {
                    object.toString().trim();
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void validar(T entidade) {
        List<AplicacaoExceptionValue> customExceptionValues = new LinkedList<>();

        for (ConstraintViolation<GenericEntity<I>> error : entidade.validationsConstraintsFails()) {

            AplicacaoExceptionValue customExceptionValue = new AplicacaoExceptionValue(
                    ValidacaoCampos.newInstance(StringUtils.substringBetween(error.getMessageTemplate(), "{", "}"),
                            error.getMessage()),
                    true, error.getPropertyPath().toString(),
                    error.getInvalidValue() != null ? error.getInvalidValue().toString() : null);

            customExceptionValues.add(customExceptionValue);
        }

        if (!customExceptionValues.isEmpty()) {
            throw new AplicacaoException(WalleValidacoes.ERRO_VALIDACAO, customExceptionValues);
        }

    }
    
    public R getRepository() {
        return repository;
    }

}