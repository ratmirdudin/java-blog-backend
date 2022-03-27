package com.ratmirdudin.jblog_server.exceptions;

import com.ratmirdudin.jblog_server.payloads.responses.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class, LockedException.class})
    public ResponseEntity<Response> handleException(Exception ex) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity.status(status)
                .body(Response.builder()
                        .timeStamp(now())
                        .errors(Map.of("exception", ex.getClass().getName()))
                        .status(status)
                        .statusCode(status.value())
                        .message("Server error")
                        .developerMessage(ex.getMessage())
                        .build()
                );
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<Response> handleAuthenticationException(AuthenticationException ex) {

        HttpStatus status = UNAUTHORIZED;

        return ResponseEntity.status(status)
                .body(Response.builder()
                        .timeStamp(now())
                        .errors(Map.of("username", "Invalid Username",
                                "password", "Invalid Password"))
                        .status(status)
                        .statusCode(status.value())
                        .message("Invalid credentials")
                        .developerMessage(ex.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Response> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status)
                .body(Response.builder()
                        .timeStamp(now())
                        .errors(createMapValidationErrors(ex))
                        .status(status)
                        .statusCode(status.value())
                        .message("Validation error")
                        .developerMessage(ex.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Response> handleBadRequestException(BadRequestException ex) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status)
                .body(Response.builder()
                        .timeStamp(now())
                        .errors(Map.of("exception", ex.getClass().getName()))
                        .status(status)
                        .statusCode(status.value())
                        .message(ex.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Response> handleException(HttpMessageNotReadableException ex) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status)
                .body(Response.builder()
                        .timeStamp(now())
                        .errors(Map.of("exception", ex.getClass().getName()))
                        .status(status)
                        .statusCode(status.value())
                        .message("Request body is missing. Send a request in the required JSON format")
                        .developerMessage(ex.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Response> handleUserNotFoundException(ResourceNotFoundException ex) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        return ResponseEntity.status(status)
                .body(Response.builder()
                        .timeStamp(now())
                        .errors(Map.of("exception", ex.getClass().getName()))
                        .status(status)
                        .statusCode(status.value())
                        .message(ex.getMessage())
                        .build()
                );
    }

    private Map<String, String> createMapValidationErrors(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();

        Map<String, String> errors = new LinkedHashMap<>();
        errors.put("exception", ex.getClass().getName());

        bindingResult.getFieldErrors()
                .forEach((item) -> {
                    String field = item.getField();
                    String message = item.getDefaultMessage();
                    String values = "";

                    if (errors.containsKey(field)) {
                        values = errors.get(field);
                        values += "; ";
                    }

                    values += message;
                    errors.put(field, values);
                });
        return errors;
    }
}
