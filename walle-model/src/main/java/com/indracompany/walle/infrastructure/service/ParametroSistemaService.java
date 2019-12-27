package com.indracompany.walle.infrastructure.service;

import org.springframework.stereotype.Service;

import com.indracompany.walle.application.service.GenericCrudServiceImpl;
import com.indracompany.walle.domain.model.ParametroSistema;
import com.indracompany.walle.infrastructure.persistence.hibernate.repository.ParametroSistemaRepository;

@Service
public class ParametroSistemaService extends GenericCrudServiceImpl<ParametroSistema, Long, ParametroSistemaRepository> {

	public ParametroSistema obterParametroSistema(String nomeParametro) {
		return repository.findByNomeParametro(nomeParametro);
	}
}
