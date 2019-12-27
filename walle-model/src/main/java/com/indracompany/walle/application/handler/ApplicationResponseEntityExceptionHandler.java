package com.indracompany.walle.application.handler;

import java.nio.file.AccessDeniedException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.indracompany.walle.application.service.exception.AplicacaoException;
import com.indracompany.walle.infrastructure.service.MessageService;
import com.indracompany.walle.presentation.dto.ResponseTO;


@ControllerAdvice
public class ApplicationResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    @Autowired
    private MessageService messageService;
    
    @ExceptionHandler({ RuntimeException.class })
    public ResponseEntity<Object> handleRuntimeException(RuntimeException exception, WebRequest request) {
        logger.error(exception.getMessage(), exception);

        return handleException(exception, HttpStatus.BAD_REQUEST, request, "resource.invalid-operation");
    }

    @ExceptionHandler({ AplicacaoException.class })
    public ResponseEntity<Object> handleAplicacaoException(AplicacaoException exception, WebRequest request) {
        return handleException(exception, HttpStatus.BAD_REQUEST, request);
    }
    
    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception, WebRequest request) {
        return handleException(exception, HttpStatus.BAD_REQUEST, request, "security.access-denied");
    }


    @ExceptionHandler({ DuplicateKeyException.class })
    public ResponseEntity<Object> handleDuplicateKeyException(DuplicateKeyException exception, WebRequest request) {
        return handleException(exception, HttpStatus.BAD_REQUEST, request, exception.getMessage());
    }

    @ExceptionHandler({ DataIntegrityViolationException.class })
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException exception, WebRequest request) {
        return handleException(exception, HttpStatus.BAD_REQUEST, request, "resource.invalid-operation");
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception, WebRequest request) {
        List<String> errors = exception.getConstraintViolations().stream()
                .map(e -> MessageFormat.format(e.getMessage(), e.getPropertyPath())).collect(Collectors.toList());
        ResponseTO<String> responseTO = new ResponseTO<>(errors);

        return handleExceptionInternal(exception, responseTO, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }


    
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        ResponseTO<List<String>> response = new ResponseTO<>();
        response.setErrors(getErrors(exception.getBindingResult()));

        return handleExceptionInternal(exception, response, headers, HttpStatus.BAD_REQUEST, request);
    }

    protected ResponseEntity<Object> handleException(Exception exception, HttpStatus status, WebRequest request, String key) {
        ResponseTO<List<String>> response = new ResponseTO<>(Arrays.asList((messageService.getMessage(key))));

        return handleExceptionInternal(exception, response, new HttpHeaders(), status, request);
    }
    
    protected ResponseEntity<Object> handleException(AplicacaoException exception, HttpStatus status, WebRequest request) {
        StringBuffer detail = new StringBuffer();
        exception.getCustomExceptionValues().forEach(ce -> {
            if(ce.getParametrosLabels() != null && !ce.getParametrosLabels().isEmpty()) {
                detail.append(ce.getParametrosLabels().toString()+"\n");
            }
        });
        Object[] key = {detail.toString()};
        String message = messageService.getMessage("erro.validacao", key);
        
        ResponseTO<List<String>> response = new ResponseTO<>(Arrays.asList((message)));

        return handleExceptionInternal(exception, response, new HttpHeaders(), status, request);
    }

    protected List<String> getErrors(BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();
        bindingResult.getFieldErrors().forEach(e -> errors.add(messageService.getMessage(e)));

        return errors;
    }
    
}
