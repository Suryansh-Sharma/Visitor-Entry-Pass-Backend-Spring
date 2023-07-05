package com.suryansh.visitorentry.exception;

import org.springframework.graphql.execution.ErrorType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RestExceptionAdvice {
    @ExceptionHandler(SpringVisitorException.class)
    public ResponseEntity<ErrorDetail> handleShowNotFoundEx(SpringVisitorException ex) {
        ErrorDetail errorDetail = new ErrorDetail(LocalDateTime.now(),ex.getType(),ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(errorDetail);
    }
    public record ErrorDetail(LocalDateTime localDateTime, ErrorType errorType, String message) {
    }

}
