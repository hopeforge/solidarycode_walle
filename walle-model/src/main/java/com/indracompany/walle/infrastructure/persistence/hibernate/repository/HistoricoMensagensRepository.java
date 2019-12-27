package com.indracompany.walle.infrastructure.persistence.hibernate.repository;

import com.indracompany.walle.domain.model.HistoricoMensagens;

public interface HistoricoMensagensRepository extends GenericCrudRepository<HistoricoMensagens, Long>{
	public HistoricoMensagens findFirstByUsuarioIdOrderByIdDesc(Long idUsuario);
}
