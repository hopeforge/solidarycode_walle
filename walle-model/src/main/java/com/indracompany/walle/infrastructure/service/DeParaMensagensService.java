package com.indracompany.walle.infrastructure.service;

import org.springframework.stereotype.Service;

import com.indracompany.walle.application.service.GenericCrudServiceImpl;
import com.indracompany.walle.domain.model.DeParaMensagens;
import com.indracompany.walle.infrastructure.persistence.hibernate.repository.DeParaMensagensRepository;

@Service
public class DeParaMensagensService extends GenericCrudServiceImpl<DeParaMensagens, Long, DeParaMensagensRepository> {
	
}
