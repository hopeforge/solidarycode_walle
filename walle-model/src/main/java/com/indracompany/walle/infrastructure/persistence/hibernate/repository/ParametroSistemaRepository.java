package com.indracompany.walle.infrastructure.persistence.hibernate.repository;

import com.indracompany.walle.domain.model.ParametroSistema;

public interface ParametroSistemaRepository extends GenericCrudRepository<ParametroSistema, Long> {

	ParametroSistema findByNomeParametro(String nomeParametro);
}
