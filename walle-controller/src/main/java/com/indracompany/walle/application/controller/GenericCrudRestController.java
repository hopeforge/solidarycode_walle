package com.indracompany.walle.application.controller;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.indracompany.walle.application.service.exception.AplicacaoException;
import com.indracompany.walle.domain.shared.GenericEntity;
import com.indracompany.walle.application.service.GenericCrudServiceImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author eder
 *
 * @param <T>
 * @param <S>
 * @param <I>
 */
@Slf4j
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public abstract class GenericCrudRestController<T extends GenericEntity<I>, I, S extends GenericCrudServiceImpl<T, I, ?>> {

    private static final long serialVersionUID = -3853594377194808570L;

    @Inject
    protected GenericCrudServiceImpl<T, I, ?> service;

    @Getter
    @Setter
    private transient T entity;

    @Getter
    @Setter
    private transient List<T> list;

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void resetar() {
        try {
            this.entity = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                    .getActualTypeArguments()[0]).newInstance();
            this.list = service.listar();
        } catch (Exception e) {
            log.error("erro ao resetar() " + this.getClass().getName(), e);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<T> salvar(
            @ApiParam(value = "Objeto entidade a ser cadastrada.", required = true) @Valid final @RequestBody T entity)
            throws AplicacaoException {

        if (log.isDebugEnabled()) {

            log.debug("Realizando a chamada do controller: " + this.getClass().getName() + ".salvar( "
                    + entity.getClass().getName() + " ). Realizando a chamada do service: "
                    + service.getClass().getName() + ".salvar( " + entity.getClass().getName() + " )");
        }

        T newEntity = service.salvarEAtualizar(entity);

        if (log.isDebugEnabled()) {

            log.debug("Chamada do controller: " + this.getClass().getName() + ".salvar( " + entity.getClass().getName()
                    + " ) realizada com sucesso.");
        }

        return new ResponseEntity<T>(newEntity, HttpStatus.OK);
    }

    @ApiOperation(value = "Atualiza uma entidade existente.", nickname = "alterar", notes = "")
    @ApiResponses(value = { @ApiResponse(code = 400, message = "ID inválido"),
            @ApiResponse(code = 404, message = "Entidade não encontrada"),
            @ApiResponse(code = 405, message = "Validation exception") })
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public @ResponseBody ResponseEntity<T> alterar(
            @ApiParam(value = "Objeto entida a ser atualizada.", required = true) @Valid final @RequestBody T entity,
            final @PathVariable I id) throws AplicacaoException {

        if (log.isDebugEnabled()) {

            log.debug("Realizando a chamada do controller: " + this.getClass().getName() + ".alterar( "
                    + entity.getClass().getName() + " , " + id + " ). Realizando a chamada do service: "
                    + service.getClass().getName() + ".salvar( " + entity.getClass().getName() + " )");
        }

        if (((Comparable<I>) id).compareTo(entity.getId()) != 0) {

            return new ResponseEntity<T>(HttpStatus.BAD_REQUEST);
        }

        service.salvarEAtualizar(entity);

        if (log.isDebugEnabled()) {

            log.debug("Chamada do controller: " + this.getClass().getName() + ".alterar( " + entity.getClass().getName()
                    + " ) realizada com sucesso.");
        }

        return new ResponseEntity<T>(entity, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> remover(final @PathVariable I id) throws AplicacaoException {

        if (log.isDebugEnabled()) {

            log.debug("Realizando a chamada do controller: " + this.getClass().getName() + ".remover( " + id
                    + " ). Realizando a chamada do service: " + service.getClass().getName() + ".remover( " + id
                    + " )");
        }

        service.remover(id);

        if (log.isDebugEnabled()) {

            log.debug("Chamada do controller: " + this.getClass().getName() + ".remover( " + id
                    + " ) realizada com sucesso.");
        }

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<List<T>> listar() throws AplicacaoException {

        if (log.isDebugEnabled()) {

            log.debug("Realizando a chamada do controller: " + this.getClass().getName()
                    + ".listar(). Realizando a chamada do service: " + service.getClass().getName() + ".obterTodos()");
        }

        List<T> result = service.listar();

        if (log.isDebugEnabled()) {

            log.debug("Chamada do controller: " + this.getClass().getName() + ".listar() realizada com sucesso.");
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { "application/json" })
    public @ResponseBody ResponseEntity<T> buscar(final @PathVariable I id) throws AplicacaoException {

        if (log.isDebugEnabled()) {

            log.debug("Realizando a chamada do controller: " + this.getClass().getName() + ".buscar( " + id
                    + " ). Realizando a chamada do service: " + service.getClass().getName() + ".obterUm( " + id
                    + " )");
        }

        T entity = service.buscar(id);

        if (log.isDebugEnabled()) {

            log.debug("Chamada do controller: " + this.getClass().getName() + ".buscar( " + id
                    + " ) realizada com sucesso.");
        }

        return entity == null ? new ResponseEntity<T>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<T>(entity, HttpStatus.OK);
    }

}
