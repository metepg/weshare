package com.weshare.exception;

import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Value
    public static class ErrorResponse {
        int status;
        String error;
        String message;
        long timestamp;

        public ErrorResponse(HttpStatus status, String message) {
            this.status = status.value();
            this.error = status.getReasonPhrase();
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        LOG.warn("AccessDeniedException: ",  ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN, "Forbidden");
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex) {
        LOG.error("Exception: ", ex);
        return new ResponseEntity<>("Jotain meni pieleen.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
