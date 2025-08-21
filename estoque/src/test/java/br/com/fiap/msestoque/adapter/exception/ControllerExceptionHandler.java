package br.com.fiap.msestoque.adapter.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import br.com.fiap.msestoque.core.exception.EstoqueException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;

class ControllerExceptionHandlerTest {

    private final ControllerExceptionHandler handler = new ControllerExceptionHandler();

    @Test
    void deveTratarConstraintViolationException() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);

        Path mockPath = mock(Path.class);
        when(mockPath.toString()).thenReturn("campoTeste");
        when(violation.getPropertyPath()).thenReturn(mockPath);
        when(violation.getMessage()).thenReturn("mensagem de erro");

        Set<ConstraintViolation<?>> violations = Set.of(violation);
        ConstraintViolationException ex = new ConstraintViolationException(violations);

        WebRequest request = new ServletWebRequest(new MockHttpServletRequest());

        var response = handler.handleConstraintViolationException(ex, request);

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    @DisplayName("Deve tratar MethodArgumentNotValidException corretamente")
    void deveTratarMethodArgumentNotValidException() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new org.springframework.validation.FieldError("obj", "campoTeste", "mensagem de erro")));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
        WebRequest request = new ServletWebRequest(new MockHttpServletRequest());

        var response = handler.handleMethodArgumentNotValid(ex, null, HttpStatus.BAD_REQUEST, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve tratar EstoqueException corretamente")
    void deveTratarEstoqueException() {
        EstoqueException ex = new EstoqueException("Estoque não encontrado");
        WebRequest request = new ServletWebRequest(new MockHttpServletRequest());

        var response = handler.handleEstoqueExceptionException(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve tratar NoHandlerFoundException corretamente")
    void deveTratarNoHandlerFoundException() throws Exception {
        NoHandlerFoundException ex = new NoHandlerFoundException("GET", "/teste", new org.springframework.http.HttpHeaders());
        WebRequest request = new ServletWebRequest(new MockHttpServletRequest());

        var response = handler.handleNoHandlerFoundException(ex, null, HttpStatus.NOT_FOUND, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}