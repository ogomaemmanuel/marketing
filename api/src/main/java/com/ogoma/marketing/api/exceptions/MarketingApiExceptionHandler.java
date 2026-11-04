package com.ogoma.marketing.api.exceptions;

import com.ogoma.marketing.core.domain.exceptions.RecordNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice

public record MarketingApiExceptionHandler() {
    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<Map<String, List<String>>> handleInvalidArgs(WebExchangeBindException ex) {
        Map<String, List<String>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                ));
        return Mono.just(errors);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public String handleRecordNotFoundException(RecordNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(Exception.class)
    public String handleRecordNotFoundException(Exception exception, ServerWebExchange webExchange) {
        log.error("Error: endpoint {}", webExchange.getRequest().getPath().value(), exception);
        return "internal server error, if this persists, contact system admin";
    }
}
