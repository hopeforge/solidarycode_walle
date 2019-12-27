package com.indracompany.walle.application.service.exception;

import java.util.ResourceBundle;

import com.indracompany.walle.infrastructure.util.Messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author eder
 * 
 */

@AllArgsConstructor
public enum WalleValidacoes implements AplicacaoExceptionValidacoes {

	// Mensagens de Erro
	ERRO_ACESSO_SISTEMA("erro.acesso.sistema", null, SEVERIDADE_ERRO), 
	ERRO_CAMPO_OBRIGATORIO("erro.campo.obrigatorio",null, SEVERIDADE_ERRO), 
	ERRO_EXCLUSAO_GENERICO("erro.exclusao.generico", null, SEVERIDADE_ERRO), 
	ERRO_OBJETO_NAO_ENCONTRADO("erro.objeto.nao.encontrado", null, SEVERIDADE_ERRO), 
	ERRO_VALIDACAO("erro.validacao",null, SEVERIDADE_ERRO),
	ERRO_SERIALIZAR_JSON("erro.serializar.json",null, SEVERIDADE_ERRO),

	// Mensagens Alterta
	ALERTA_NENHUM_REGISTRO_ENCONTRADO("alerta.nenhum.registro.encontrado", null, SEVERIDADE_ALERTA), ;

	public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");

	@Getter @Setter private String codigoMsg;
	@Getter @Setter private String codigoMsgAuxiliar;
	@Getter @Setter private Integer severidade = SEVERIDADE_ERRO;

	public static WalleValidacoes carregarPorCodigoMsg(String codigo) {
		for (WalleValidacoes co : WalleValidacoes.values()) {
			if (codigo.equals(co.getCodigoMsg())) {
				return co;
			}
		}
		return null;
	}

	public String getDescricaoMsg(String... params) {
		return Messages.getMessage(RESOURCE_BUNDLE, this.getCodigoMsg(), params);
	}

	public String getDescricaoMsgAuxiliar(String... params) {
		return Messages.getMessage(RESOURCE_BUNDLE, this.getCodigoMsgAuxiliar(), params);
	}

	@Override
	public String getCodigoMsg() {
		return null;
	}

	@Override
	public String getCodigoMsgAuxiliar() {
		return null;
	}

	@Override
	public Integer getSeveridade() {
		return null;
	}

}
