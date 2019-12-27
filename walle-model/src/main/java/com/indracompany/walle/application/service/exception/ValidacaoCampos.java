package com.indracompany.walle.application.service.exception;

import org.springframework.util.Assert;

import com.indracompany.walle.infrastructure.util.Messages;


/**
 * @author eder
 *
 */
public final class ValidacaoCampos implements AplicacaoExceptionValidacoes {

	private String codigoMsg;
	private String descricao;

	private ValidacaoCampos(String codigoMsg, String descricao) {
		this.codigoMsg = codigoMsg;
		this.descricao = descricao;
	}

	@Override
	public String getCodigoMsg() {
		return codigoMsg;
	}

	@Override
	public String getCodigoMsgAuxiliar() {
		return null;
	}

	@Override
	public Integer getSeveridade() {
		return AplicacaoExceptionValidacoes.SEVERIDADE_ALERTA;
	}

	/**
	 *
	 * Obtém a descrição da Mensagem deste Enum.
	 *
	 * @return int
	 */
	public String getDescricaoMsg(String... params) {
		return Messages.getTextMessageReplace(descricao, params);
	}

	public static ValidacaoCampos newInstance(String codigoMsg, String descricao) {

		Assert.notNull(codigoMsg, "A decricao da mensagem é obrigatorio");

		return new ValidacaoCampos(codigoMsg, descricao);
	}

	@Override
	public String getDescricaoMsgAuxiliar(String... params) {
		return null;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setCodigoMsg(String codigoMsg) {
		this.codigoMsg = codigoMsg;
	}

}
