package com.tresende.catalog.infrastructure.utils;

import com.tresende.catalog.domain.exceptions.InternalErrorException;
import com.tresende.catalog.infrastructure.exceptions.NotFoundException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient.ResponseSpec.ErrorHandler;

import java.net.http.HttpTimeoutException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface HttpClient {

    Predicate<HttpStatusCode> is5xx = HttpStatusCode::is5xxServerError;
    Predicate<HttpStatusCode> isNotFound = HttpStatus.NOT_FOUND::equals;

    String namespace();

    default ErrorHandler notFoundErrorHandler(final String id) {
        return (req, res) -> {
            throw NotFoundException.with("%s of id %s was not found".formatted(namespace(), id));
        };
    }

    default ErrorHandler a5xxHandler(final String id) {
        return (req, res) -> {
            throw InternalErrorException.with("Failed to get %s of id %s".formatted(namespace(), id));
        };
    }

    default <T> Optional<T> doGet(final String id, Supplier<T> fn) {
        try {
            return Optional.ofNullable(fn.get());
        } catch (NotFoundException ex) {
            return Optional.empty();
        } catch (ResourceAccessException ex) {
            final var cause = ExceptionUtils.getRootCause(ex);
            if (cause instanceof HttpTimeoutException || cause instanceof TimeoutException) {
                throw InternalErrorException.with("Timeout on get %s of id %s".formatted(namespace(), id));
            }
            throw ex;
        }
    }
}
