package com.indracompany.walle.domain.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.indracompany.walle.domain.shared.GenericEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "HISTORICO_MENSAGENS")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class HistoricoMensagens extends GenericEntity<Long> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "ID_USUARIO")
	private UsuarioWhats usuario;
	
	@ManyToOne
	@JoinColumn(name = "ID_MENSAGEM")
	private Mensagem mensagem;

}
