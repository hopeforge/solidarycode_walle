package com.indracompany.walle.infrastructure.persistence.hibernate.repository;

import com.indracompany.walle.domain.model.UsuarioWhats;

public interface UsuarioWhatsRepository extends GenericCrudRepository<UsuarioWhats, Long> {
	public UsuarioWhats findByRemoteJid(String remoteJid);
}
