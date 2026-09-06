package ru.practicum.shareit.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void shouldHandleNotFoundException() {
        NotFoundException ex = new NotFoundException("Ресурс не найден");
        ResponseEntity<ErrorResponse> response = handler.handleNotFoundException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("Ресурс не найден");
    }

    @Test
    void shouldHandleValidationException() {
        ValidationException ex = new ValidationException("Ошибка валидации");
        ResponseEntity<ErrorResponse> response = handler.handleValidation(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("Ошибка валидации");
    }

    @Test
    void shouldHandleConflictException() {
        ConflictException ex = new ConflictException("Конфликт данных");
        ResponseEntity<ErrorResponse> response = handler.handleConflictException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("Конфликт данных");
    }

    @Test
    void shouldHandleForbiddenException() {
        ForbiddenException ex = new ForbiddenException("Доступ запрещен");
        ResponseEntity<ErrorResponse> response = handler.handleForbiddenException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("Доступ запрещен");
    }

    @Test
    void shouldHandleMethodArgumentNotValidException() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldHandleGenericException() {
        Exception ex = new RuntimeException("Внутренняя ошибка сервера");
        ResponseEntity<ErrorResponse> response = handler.handleServerException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("Внутренняя ошибка сервера");
    }
}
