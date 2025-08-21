package br.com.fiap.mspedidoreciver.adapter.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

class ControllerExceptionHandlerTest {

    private final ControllerExceptionHandler handler = new ControllerExceptionHandler();

    @Test
    void deveTratarConstraintViolationException() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);

        jakarta.validation.Path path = mock(jakarta.validation.Path.class);
        when(path.toString()).thenReturn("campoTeste");

        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("Campo obrigatório");

        ConstraintViolationException ex = new ConstraintViolationException(Set.of(violation));
        WebRequest request = new ServletWebRequest(new MockHttpServletRequest());

        ResponseEntity<Object> response = handler.handleConstraintViolationException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Problem body = (Problem) response.getBody();
        assertNotNull(body);
        assertEquals("Dados inválidos", body.getTitle());
        assertEquals(1, body.getFields().size());
        assertEquals("campoTeste", body.getFields().get(0).getName());
        assertEquals("Campo obrigatório", body.getFields().get(0).getUserMessage());
    }


    @Test
    void deveTratarMethodArgumentNotValidException() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("obj", "nome", "Nome é obrigatório");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
        WebRequest request = new ServletWebRequest(new MockHttpServletRequest());

        ResponseEntity<Object> response = handler.handleMethodArgumentNotValid(
                ex,
                null,
                HttpStatus.BAD_REQUEST,
                request
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Problem body = (Problem) response.getBody();
        assertNotNull(body);
        assertEquals("Dados inválidos", body.getTitle());
        assertEquals("nome", body.getFields().get(0).getName());
        assertEquals("Nome é obrigatório", body.getFields().get(0).getUserMessage());
    }

    @Test
    void deveTratarNoHandlerFoundException() {
        NoHandlerFoundException ex = new NoHandlerFoundException("GET", "/nao-existe", null);
        WebRequest request = new ServletWebRequest(new MockHttpServletRequest());

        ResponseEntity<Object> response = handler.handleNoHandlerFoundException(
                ex,
                null,
                HttpStatus.NOT_FOUND,
                request
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Problem body = (Problem) response.getBody();
        assertNotNull(body);
        assertEquals("Recurso não encontrado", body.getTitle());
        assertTrue(body.getDetail().contains("/nao-existe"));
    }
}
