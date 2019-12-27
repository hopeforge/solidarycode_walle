package com.indracompany.walle.infrastructure.service;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.indracompany.walle.application.service.GenericCrudServiceImpl;
import com.indracompany.walle.domain.model.UsuarioWhats;
import com.indracompany.walle.infrastructure.persistence.hibernate.repository.UsuarioWhatsRepository;

@Service
public class UsuarioWhatsService extends GenericCrudServiceImpl<UsuarioWhats, Long, UsuarioWhatsRepository> {

	@Autowired
	UsuarioWhatsRepository usuarioWhatsRepository;

	public UsuarioWhats createUsuario(String jid) {
		if (StringUtils.isNotBlank(jid) && Objects.isNull(usuarioWhatsRepository.findByRemoteJid(jid))) {
			UsuarioWhats usuarioWhats = new UsuarioWhats();
			usuarioWhats.setRemoteJid(jid);
			return salvar(usuarioWhats);
		}
		return usuarioWhatsRepository.findByRemoteJid(jid);
	}

}
