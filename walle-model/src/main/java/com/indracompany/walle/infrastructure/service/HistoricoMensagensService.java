package com.indracompany.walle.infrastructure.service;

import org.springframework.stereotype.Service;

import com.indracompany.walle.application.service.GenericCrudServiceImpl;
import com.indracompany.walle.domain.model.HistoricoMensagens;
import com.indracompany.walle.infrastructure.persistence.hibernate.repository.HistoricoMensagensRepository;

@Service
public class HistoricoMensagensService extends GenericCrudServiceImpl<HistoricoMensagens, Long, HistoricoMensagensRepository> {
	
}
