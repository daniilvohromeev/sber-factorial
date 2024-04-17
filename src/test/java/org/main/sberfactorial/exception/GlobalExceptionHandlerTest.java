package org.main.sberfactorial.exception;

import org.junit.jupiter.api.Test;
import org.main.sberfactorial.exception.payload.ErrorResponse;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private BindingResult bindingResult;
    @Test
    public void testHandleInvalidFactorialArgumentException() {

        String errorMessage = "Invalid number provided";
        InvalidFactorialArgumentException exception = new InvalidFactorialArgumentException(errorMessage);
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleInvalidFactorialArgumentException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().getError());
    }

    @Test
    public void testHandleCacheInvalidException() {

        String errorMessage = "Cache error occurred";
        CacheInvalidException exception = new CacheInvalidException(errorMessage);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleCacheInvalidException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().getError());
    }

    @Test
    public void testHandleValidationExceptions() {

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);
        FieldError fieldError = new FieldError("objectName", "fieldName", "error message");

        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<Object> response = globalExceptionHandler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Статус код должен быть BAD REQUEST");
        assertNotNull(response.getBody(), "Тело ответа не должно быть null");
        assertInstanceOf(Map.class, response.getBody(), "Тело ответа должно быть картой");

        if (response.getBody() instanceof Map<?, ?> errors) {

            assertTrue(errors.containsKey("fieldName"), "Карта ошибок должна содержать 'fieldName'");

            Object errorMessage = errors.get("fieldName");

            assertInstanceOf(String.class, errorMessage, "Сообщение об ошибке должно быть строкой");
            assertEquals("error message", errorMessage, "Сообщение об ошибке должно соответствовать");
        } else {
            fail("Тело ответа не является картой");
        }
    }
}