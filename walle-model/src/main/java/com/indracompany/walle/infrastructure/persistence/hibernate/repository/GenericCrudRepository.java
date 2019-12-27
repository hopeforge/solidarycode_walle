package com.indracompany.walle.infrastructure.persistence.hibernate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.indracompany.walle.domain.shared.GenericEntity;

/**
 * @author eder
 *
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface GenericCrudRepository<T extends GenericEntity<I>, I> extends JpaRepository<T, I>, JpaSpecificationExecutor<T> {

}
