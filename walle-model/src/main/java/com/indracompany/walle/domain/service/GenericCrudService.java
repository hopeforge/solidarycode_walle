package com.indracompany.walle.domain.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.indracompany.walle.domain.shared.GenericEntity;

public interface GenericCrudService<T extends GenericEntity<I>, I> {
    
    public T salvar(T entidade);
    
    public T atualizar(I id, T entity);
    
    public Long contar();
    
    public List<T> listar();
    
    public Page<T> listar(Specification<T> specification, Pageable pageable);
    
    public T buscar(I id);
    
    public void remover(T entidade);
    
    public void remover(I id);

}
