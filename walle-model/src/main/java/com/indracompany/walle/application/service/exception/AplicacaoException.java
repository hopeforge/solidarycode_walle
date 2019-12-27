package com.indracompany.walle.application.service.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen")
public class AplicacaoException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private transient List<AplicacaoExceptionValue> customExceptionValues = new LinkedList<>();
	private static final String MSG_DEFAULT = "Erro de Validação!";
	
	
	public AplicacaoException(AplicacaoExceptionValidacoes validacao) {
		super(validacao.getCodigoMsg());
		this.customExceptionValues.add(new AplicacaoExceptionValue(validacao));
	}

	public AplicacaoException(AplicacaoExceptionValidacoes validacao, String... params) {
		super(validacao.getCodigoMsg());
		this.customExceptionValues.add(new AplicacaoExceptionValue(validacao, params));
	}

	public AplicacaoException(AplicacaoExceptionValidacoes validacao, Throwable throwable, String... params) {
		super(validacao.getCodigoMsg(), throwable);
		this.customExceptionValues.add(new AplicacaoExceptionValue(validacao, params));
	}

	public AplicacaoException(List<String> detalheErrosValidacao) {
		super(MSG_DEFAULT);
		this.customExceptionValues.add(new AplicacaoExceptionValue(detalheErrosValidacao));
	}

	public AplicacaoException(AplicacaoExceptionValidacoes validacao,
			List<AplicacaoExceptionValue> customExceptionValues) {
		this(validacao);

		if (customExceptionValues != null) {
			this.customExceptionValues.addAll(customExceptionValues);
		}
	}

	public AplicacaoExceptionValue getCustomExceptionValue() {
		return customExceptionValues.get(0);
	}

	public List<AplicacaoExceptionValue> getCustomExceptionValues() {
		return customExceptionValues;
	}

	public void setCustomExceptionValues(List<AplicacaoExceptionValue> customExceptionValues) {
		this.customExceptionValues = customExceptionValues;
	}

	/**
	 * Retorna a pilha de erros da exceção.
	 *
	 * @param exception
	 * @return Uma String com a pilha de erros.
	 */
	public static final String getPilhaErro(Throwable t) {
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		t.printStackTrace(printWriter);
		return writer.toString();

	}

	/**
	 * Verifica se o Throwable passado por parametro contém uma exceção do tipo
	 * {@link AplicacaoException}, caso contrário retorna null.
	 *
	 * Obs: Este método retorna o PRIMEIRO {@link AplicacaoException} encontrado na
	 * stack (pilha), ou seja, o ÚLTIMO {@link AplicacaoException} capturado (catch)
	 * durante o lançamento do primeiro throws.
	 *
	 * @author Eder
	 *
	 * @param throwable
	 *            A exceção a ser analisada.
	 * @return Uma exceção de negócio ou null caso a exceção passada como parâmetro
	 *         não contenha uma instância de {@link AplicacaoException}.
	 */
	public static final AplicacaoException getPrimeiroCustomException(Throwable throwable) {
		AplicacaoException retorno = null;
		if (throwable != null) {
			if (throwable instanceof AplicacaoException) {
				retorno = (AplicacaoException) throwable;
			} else {
				retorno = getPrimeiroCustomException(throwable.getCause());
			}
		}
		return retorno;
	}

	/**
	 * Verifica se o Throwable passado por parametro contém uma exceção do tipo
	 * {@link AplicacaoException}, caso contrário retorna null.
	 *
	 * Obs: Este método retorna o ÚLTIMO {@link AplicacaoException} encontrado na
	 * stack (pilha), ou seja, o PRIMEIRO {@link AplicacaoException} capturado
	 * (catch) no lançamento do primeiro throws, que provavelmente será a origem do
	 * erro. Este método é util para identificar se um {@link AplicacaoException}
	 * contém outro {@link AplicacaoException} encapsulado.
	 *
	 *
	 * @param throwable
	 *            A exceção a ser analisada.
	 * @return Uma exceção de negócio ou null caso a exceção passada como parâmetro
	 *         não contenha uma instância de {@link AplicacaoException}.
	 */
	public static final AplicacaoException getUltimoCustomException(Throwable throwable) {

		AplicacaoException retorno = null;

		if (throwable instanceof AplicacaoException) {
			retorno = (AplicacaoException) throwable;
			if (throwable.getCause() instanceof AplicacaoException) {
				retorno = getUltimoCustomException(throwable.getCause());
			}
		}
		return retorno;
	}

}