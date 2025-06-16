package com.weshare.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("Handles AccessDeniedException with 403 response")
    void handleAccessDeniedException_returnsForbidden() {
        AccessDeniedException ex = new AccessDeniedException("denied");

        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = handler.handleAccessDeniedException(ex);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        Object body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.FORBIDDEN.value(), (int) getField(body, "status"));
        assertEquals("Forbidden", getField(body, "error"));
        assertEquals("Forbidden", getField(body, "message"));
        assertTrue((long) getField(body, "timestamp") > 0);
    }

    @Test
    @DisplayName("Handles UsernameNotFoundException with 404 response")
    void handleUsernameNotFoundException_returnsNotFound() {
        UsernameNotFoundException ex = new UsernameNotFoundException("user");

        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = handler.handleUsernameNotFoundExceptionException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Object body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.NOT_FOUND.value(), (int) getField(body, "status"));
        assertEquals("Not Found", getField(body, "error"));
        assertEquals("User not found", getField(body, "message"));
        assertTrue((long) getField(body, "timestamp") > 0);
    }

    @Test
    @DisplayName("Handles generic Exception with 500 response")
    void handleAllExceptions_returnsInternalServerError() {
        Exception ex = new RuntimeException("something failed");

        ResponseEntity<Object> response = handler.handleAllExceptions(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Jotain meni pieleen.", response.getBody());
    }

    private Object getField(Object obj, String fieldName) {
        Field field = ReflectionUtils.findField(obj.getClass(), fieldName);
        assertNotNull(field);
        field.setAccessible(true);
        return ReflectionUtils.getField(field, obj);
    }
}
