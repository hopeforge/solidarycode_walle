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
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "USUARIOWHATS")
@Getter
@Setter
public class UsuarioWhats extends GenericEntity<Long> {

	private static final long serialVersionUID = 6135914955747956943L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "Jid")
	private String remoteJid;

}
