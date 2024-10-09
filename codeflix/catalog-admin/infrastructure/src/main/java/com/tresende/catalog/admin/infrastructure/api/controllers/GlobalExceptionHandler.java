package com.tresende.catalog.admin.infrastructure.api.controllers;

import com.tresende.catalog.admin.domain.exceptions.DomainException;
import com.tresende.catalog.admin.domain.validation.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;


@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = DomainException.class)
    public ResponseEntity<?> handleDomainException(final DomainException ex) {

        return ResponseEntity.unprocessableEntity().body(ApiError.fromException(ex));
    }

    static record ApiError(
            String message,
            List<Error> errors
    ) {

        public static ApiError fromException(DomainException ex) {
            return new ApiError(ex.getMessage(), ex.getErrors());
        }
    }
}
