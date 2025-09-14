package com.tresende.catalog.infrastructure.utils;

import com.tresende.catalog.domain.exceptions.InternalErrorException;
import com.tresende.catalog.infrastructure.exceptions.NotFoundException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient.ResponseSpec.ErrorHandler;

import java.net.http.HttpConnectTimeoutException;
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
            throw NotFoundException.with("Not found observed from %s [resourceId:%s]".formatted(namespace(), id));
        };
    }

    default ErrorHandler a5xxHandler(final String id) {
        return (req, res) -> {
            throw InternalErrorException.with("Error observed from %s [resourceId:%s] [status:%s]".formatted(namespace(), id, res.getStatusCode().value()));
        };
    }

    default <T> Optional<T> doGet(final String id, Supplier<T> fn) {
        try {
            return Optional.ofNullable(fn.get());
        } catch (NotFoundException ex) {
            return Optional.empty();
        } catch (ResourceAccessException ex) {
            throw handleResourceAccessException(id, ex);
        } catch (Throwable t) {
            throw handleThrowable(id, t);
        }
    }

    private InternalErrorException handleThrowable(final String id, final Throwable t) {
        if (t instanceof InternalErrorException ex) {
            throw ex;
        }
        return InternalErrorException.with("Unhandled error observed from %s [resourceId:%s]".formatted(namespace(), id), t);
    }

    private InternalErrorException handleResourceAccessException(final String id, ResourceAccessException ex) {
        final var cause = ExceptionUtils.getRootCause(ex);
        if (cause instanceof HttpConnectTimeoutException) {
            return InternalErrorException.with("ConnectTimeout observed from %s [resourceId:%s]".formatted(namespace(), id), ex);
        }
        if (cause instanceof HttpTimeoutException || cause instanceof TimeoutException) {
            return InternalErrorException.with("Timeout observed from %s [resourceId:%s]".formatted(namespace(), id), ex);
        }
        throw ex;
    }
}
