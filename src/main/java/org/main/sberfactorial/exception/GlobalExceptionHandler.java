package org.main.sberfactorial.exception;

import io.micrometer.core.annotation.Timed;
import org.main.sberfactorial.exception.payload.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Глобальный обработчик исключений для приложения.
 * <p>
 * Этот класс {@link ControllerAdvice} предназначен для централизованной обработки исключений, возникающих
 * в различных частях приложения. Он перехватывает определенные исключения и возвращает стандартизированные
 * ответы HTTP с соответствующими сообщениями об ошибке.
 * </p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключения {@link InvalidFactorialArgumentException} возникающие при некорректных
     * аргументах запроса на расчет факториала.
     * <p>
     * Этот метод перехватывает исключения, связанные с некорректными параметрами запроса на расчет факториала,
     * и возвращает ответ с HTTP статусом 400 (Bad Request) и соответствующим сообщением об ошибке.
     * </p>
     *
     * @param ex перехваченное исключение типа {@link InvalidFactorialArgumentException}.
     * @return ResponseEntity с телом ответа, содержащим информацию об ошибке.
     */

    @Timed(value = "validation.method.argument")
    @ExceptionHandler(InvalidFactorialArgumentException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFactorialArgumentException(InvalidFactorialArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Обрабатывает исключения {@link CacheInvalidException} возникающие при ошибках доступа или работы кэша.
     * <p>
     * При возникновении проблем с кэшем, например, когда кэш недоступен или его данные некорректны, этот метод
     * перехватывает соответствующее исключение и возвращает ответ с HTTP статусом 400 (Bad Request) и сообщением об ошибке.
     * </p>
     *
     * @param ex перехваченное исключение типа {@link CacheInvalidException}.
     * @return ResponseEntity с телом ответа, содержащим информацию об ошибке.
     */
    @Timed("validation.cache.invalid")
    @ExceptionHandler(CacheInvalidException.class)
    public ResponseEntity<ErrorResponse> handleCacheInvalidException(CacheInvalidException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    /**
     * Обрабатывает {@link MethodArgumentNotValidException}, возникающие при валидационных ошибках
     * данных запроса.
     * <p>
     * При наличии ошибок валидации входных данных метода, этот обработчик собирает информацию об
     * всех ошибках валидации и возвращает ее в виде структурированного ответа с HTTP статусом 400
     * (Bad Request). Это помогает клиентам корректно исправить данные в запросах.
     * </p>
     *
     * @param ex перехваченное исключение типа {@link MethodArgumentNotValidException}, содержащее
     *           информацию о нарушениях валидации.
     * @return ResponseEntity содержащий карту ошибок валидации.
     */
    @Timed("validation.method.argument")
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}