package com.indracompany.walle.infrastructure.persistence.hibernate.repository;

import com.indracompany.walle.domain.model.DeParaMensagens;

public interface DeParaMensagensRepository extends GenericCrudRepository<DeParaMensagens, Long> {
	public DeParaMensagens findByMensagemDeIdAndResposta(Long mensagemDeId, String resposta);
}
