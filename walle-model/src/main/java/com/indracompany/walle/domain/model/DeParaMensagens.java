package com.indracompany.walle.domain.model;

import javax.persistence.Column;
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
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "DE_PARA_MENSAGENS")
@Getter
@Setter
public class DeParaMensagens extends GenericEntity<Long> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "MENSAGEM_DE")
	private Mensagem mensagemDe;
	
	@Column(name = "RESPOSTA")
	private String resposta;
	
	@ManyToOne
	@JoinColumn(name = "MENSAGEM_PARA")
	private Mensagem mensagemPara;
}
