package com.indracompany.walle.application.service.exception;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ExceptionVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String codigo;
	private String tipo = "erro";
	private Set<String> detalhes;
	private Integer httpStatus;


	public ExceptionVO() {
		this.detalhes = new HashSet<>();
	}

	public ExceptionVO(String codigo, Set<String> detalhes) {
		super();
		this.codigo = codigo;

		if (detalhes == null) {
			detalhes = new HashSet<>();
		}

		this.detalhes = detalhes;
	}

	public ExceptionVO(String error, String detalhe) {
		super();
		this.codigo = error;

		String[] partes = detalhe.split(System.getProperty("line.separator"));
		this.detalhes = new HashSet<String>();
		if (partes.length >= 0) {
		

			for (String s : partes) {
				this.detalhes.add(s);
			}

		} else {
			 this.detalhes.add(detalhe);
		}

	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String error) {
		this.codigo = error;
	}

	public Set<String> getDetalhes() {
		return Collections.unmodifiableSet(this.detalhes);
	}

	public void addDetalhe(String detalhe) {
		this.detalhes.add(detalhe);
	}

	public void addDetalhes(Set<String> detalhes) {
		this.detalhes.addAll(detalhes);
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String alerta) {
		this.tipo = alerta;
	}

	public Integer getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(Integer httpStatus) {
		this.httpStatus = httpStatus;
	}


}