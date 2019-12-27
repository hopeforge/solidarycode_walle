package com.indracompany.walle.application.service.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.indracompany.walle.infrastructure.util.Messages;


/**
 * @author eder
 *
 */
public class AplicacaoExceptionValue {

	private AplicacaoExceptionValidacoes validacao;

	private List<String> parametros = new ArrayList<String>();

	private List<String> detalheErrosValidacao = new LinkedList<String>();

	private boolean campoView;

	public AplicacaoExceptionValue(AplicacaoExceptionValidacoes validacao) {
		this.validacao = validacao;
	}

	public AplicacaoExceptionValue(AplicacaoExceptionValidacoes validacao, String... params) {
		this.validacao = validacao;
		this.parametros = Arrays.asList(params);
	}

	public AplicacaoExceptionValue(AplicacaoExceptionValidacoes validacao, boolean campoView, String... params) {
		this.validacao = validacao;
		this.campoView = campoView;
		this.parametros = Arrays.asList(params);
	}

	public AplicacaoExceptionValue(List<String> detalheErrosValidacao) {
		this.detalheErrosValidacao.addAll(detalheErrosValidacao);
	}

	public AplicacaoExceptionValidacoes getValidacao() {
		return validacao;
	}

	public void setValidacao(AplicacaoExceptionValidacoes validacao) {
		this.validacao = validacao;
	}

	public List<String> getParametros() {
		return parametros;
	}

	public void setParametros(List<String> parametros) {
		this.parametros = parametros;
	}

	public List<String> getDetalheErrosValidacao() {
		return detalheErrosValidacao;
	}

	public void setDetalheErrosValidacao(List<String> detalheErrosValidacao) {
		this.detalheErrosValidacao = detalheErrosValidacao;
	}

	public List<String> getParametrosLabels() {
		List<String> parametrosLabels = new LinkedList<String>();
		if (campoView) {
			if (parametros != null) {
				for (String param : parametros) {
					if (param != null) {
						String[] campos = param.split("\\|");
						String label = "";
						for (String c : campos) {
							if (!label.isEmpty()) {
								label += ", ";
							}
							String message = Messages.getMessage("label." + c);
							if (message != null) {
								label += message;
							} else {
								label = c;
							}

						}
						parametrosLabels.add(label);
					}
				}
			}
		}
		return parametrosLabels;
	}

	public boolean isCampoView() {
		return campoView;
	}

	public void setCampoView(boolean campoView) {
		this.campoView = campoView;
	}

}