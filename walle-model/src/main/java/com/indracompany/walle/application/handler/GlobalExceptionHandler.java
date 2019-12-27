package com.indracompany.walle.application.handler;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.indracompany.walle.application.service.exception.AplicacaoException;
import com.indracompany.walle.application.service.exception.AplicacaoExceptionValidacoes;
import com.indracompany.walle.application.service.exception.AplicacaoExceptionValue;
import com.indracompany.walle.application.service.exception.WalleValidacoes;
import com.indracompany.walle.application.service.exception.ExceptionVO;
import com.indracompany.walle.infrastructure.util.JSONUtil;

import lombok.extern.slf4j.Slf4j;



@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = {
            ServletException.class,
            AplicacaoException.class,
            HttpClientErrorException.class,
            HttpServerErrorException.class,
            JpaSystemException.class,
            IllegalArgumentException.class,
            Exception.class
        })
    public ResponseEntity<Object> handleException(final Throwable ex, final WebRequest request) {

        /*
         * realizando a impressão da exception no log do servidor
         */
        imprimirHandleException(ex, request);

        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (ex instanceof ServletException) {

            return handleExceptionInternal((ServletException) ex, null, headers, status, request);
        } else if (ex instanceof TransactionSystemException) {

            return handleAplicacaoException((AplicacaoException) ((TransactionSystemException)
                    ex).getApplicationException(), headers, status, request);
        } else if (ex instanceof AplicacaoException) {

            return handleAplicacaoException((AplicacaoException) ex, headers, status, request);
        } else if (ex instanceof HttpClientErrorException) {

            return handleHttpStatusCodeException((HttpClientErrorException) ex, headers, status,
                    request);
        } else if (ex instanceof HttpServerErrorException) {

            return handleHttpStatusCodeException((HttpServerErrorException) ex, headers, status,
                    request);
        } else if (ex instanceof JpaSystemException) {

            return handleJpaSystemException((JpaSystemException) ex, headers, status, request);
        } else if (ex instanceof IllegalArgumentException) {

            return handleIllegalArgumentException((IllegalArgumentException) ex, headers, status,
                    request);
        } else {

            log.warn("Unknown exception type: " + ex.getClass().getName(), ex);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            return handleExceptionInternal((Exception) ex, null, headers, status, request);
        }
    }


    /**
     * Customize the response for IllegalArgumentException.
     * This method delegates to {@link #handleExceptionInternal(Exception, Object, HttpHeaders,
     * HttpStatus, WebRequest)}.
     * @param ex the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<Object> handleIllegalArgumentException(
            final IllegalArgumentException ex, final HttpHeaders headers, final HttpStatus status,
            final WebRequest request) {

        String message = ex.getMessage();
        ExceptionVO body = new ExceptionVO("Parâmetros inválidos.", message);

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    /**
     * Customize the response for JpaSystemException.
     * This method delegates to {@link #handleExceptionInternal(Exception, Object, HttpHeaders,
     * HttpStatus, WebRequest)}.
     * @param ex the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<Object> handleJpaSystemException(final JpaSystemException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        ExceptionVO body = null;
        if (ex.getCause() instanceof PersistenceException
                && ((PersistenceException) ex.getCause()).getCause()
                        instanceof ConstraintViolationException) {

            body = new ExceptionVO("O item n\u00e3o pode ser manuseado.",
                    "Atualmente refer\u00eancias vinculadas ao item impedem essa a\u00e7\u00e3o.");
        }

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    /**
     * Customize the response for HttpClientErrorException.
     * This method delegates to {@link #handleExceptionInternal(Exception, Object, HttpHeaders,
     * HttpStatus, WebRequest)}.
     * @param ex the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<Object> handleHttpStatusCodeException(final HttpStatusCodeException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        ExceptionVO body = null;

        try {

            body = JSONUtil.convertJsonToObject(ex.getResponseBodyAsByteArray(), ExceptionVO.class);
        } catch (Exception e) {
            e.printStackTrace();
            body = null;
        }

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    /**
     * Customize the response for AplicacaoException.
     * This method logs a warning, sets the "Allow" header, and delegates to
     * {@link #handleExceptionInternal(Exception, Object, HttpHeaders, HttpStatus, WebRequest)}.
     * @param ex the exception
     * @param headers the headers to be written to the response
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<Object> handleAplicacaoException(final AplicacaoException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        ExceptionVO body = new ExceptionVO();
        body.setCodigo(ex.getCustomExceptionValue().getValidacao().getCodigoMsg());
        int severidade = ex.getCustomExceptionValue().getValidacao().getSeveridade();

        if (severidade == AplicacaoExceptionValidacoes.SEVERIDADE_ALERTA) {
            body.setTipo("alerta");
        }

        for (AplicacaoExceptionValue cve : ex.getCustomExceptionValues()) {

            if (cve.getValidacao() != null && !cve.getValidacao().getCodigoMsg().isEmpty()) {

                List<String> params = null;
                if (cve.isCampoView()) {
                    params = cve.getParametrosLabels();
                } else {
                    params = cve.getParametros();
                }

                String mensagem = cve.getValidacao().getDescricaoMsg(params != null
                        ? params.toArray(new String[params.size()]) : null);

                String mensagemAux = cve.getValidacao().getDescricaoMsgAuxiliar(params != null
                        ? params.toArray(new String[params.size()]) : null);

                if (StringUtils.isNotEmpty(mensagemAux)) {
                    mensagem += " - " + mensagemAux;
                }

                body.addDetalhe(mensagem);

                if (!CollectionUtils.isEmpty(cve.getDetalheErrosValidacao())) {

                    body.getDetalhes().addAll(cve.getDetalheErrosValidacao());
                }

            } else {

                body.setHttpStatus(status.value());
                log.error("Ausencia de parametros em " + AplicacaoException.class.getSimpleName());
            }
        }

        return handleExceptionInternal(ex, body, headers, status, request);
    }


    /**
     * A single place to customize the response body of all Exception types.
     * This method returns {@code null} by default.
     * @param ex the exception
     * @param body the body to use for the response
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     */
    protected ResponseEntity<Object> handleExceptionInternal(final Exception ex, final Object body,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute("javax.servlet.error.exception", ex, WebRequest.SCOPE_REQUEST);
        }

        ExceptionVO exceptionVO = null;

        if (body != null && ExceptionVO.class.isInstance(body)) {

            exceptionVO = (ExceptionVO) body;
        } else {

            exceptionVO = new ExceptionVO();
            exceptionVO.setCodigo(WalleValidacoes.ERRO_ACESSO_SISTEMA.getCodigoMsg());
            exceptionVO.addDetalhe(WalleValidacoes.ERRO_ACESSO_SISTEMA.getDescricaoMsg());
        }


        return new ResponseEntity<Object>(exceptionVO, headers, status);
    }

    private void imprimirHandleException(final Throwable ex, final WebRequest request) {

        log.error(ExceptionUtils.getMessage(ex), ex);
    }

}
