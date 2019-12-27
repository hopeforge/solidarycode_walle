package com.indracompany.walle.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.indracompany.walle.domain.shared.GenericEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "MENSAGENS")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Mensagem extends GenericEntity<Long> {
	
	private static final long serialVersionUID = 5849085922239791517L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "DESC_MENSAGEM")
	private String descMensagem;
	
	@Column(name = "GERA_ARQUIVO")
	private String arquivo;
	
}
