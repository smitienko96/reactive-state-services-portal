package test.common.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import test.common.service.ElementNotFoundException;
import test.common.service.ElementNotUniqueException;
import test.documents.domain.published.Error;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author s.smitienko
 */
@ControllerAdvice
public class GlobalErrorWebExceptionHandler {

    @ExceptionHandler
    public Mono<ServerResponse> handleConflict(ElementNotUniqueException exception) {
        Error errorObject = new Error();
        errorObject.setDescription(exception.getMessage());
        errorObject.setEntityIdentifier(exception.identifier());
        errorObject.setStackTrace(Arrays.stream(exception.getStackTrace())
                .map(s -> s.toString()).collect(Collectors.toList()));
        return buildResponse(errorObject, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public Mono<ServerResponse> handleNotFound(ElementNotFoundException exception) {
        Error errorObject = new Error();
        errorObject.setDescription(exception.getMessage());
        errorObject.setEntityIdentifier(exception.identifier());
        return buildResponse(errorObject, HttpStatus.NOT_FOUND);
    }

    private Mono<ServerResponse> buildResponse(Error errorObject,
                                               HttpStatus status) {
        return ServerResponse.status(status).contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(errorObject));
    }
}
