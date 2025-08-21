package br.com.fiap.tech_challenge_4a_fase_cliente.adapter.exception;

import br.com.fiap.tech_challenge_4a_fase_cliente.TechChallenge4aFaseClienteApplication;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.exception.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TechChallenge4aFaseClienteApplication.class)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes Unitários do Handler de Exceções do Controller")
class ControllerExceptionHandlerTest {

    private ControllerExceptionHandler exceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new ControllerExceptionHandler();
        webRequest = mock(WebRequest.class);
    }

    @Test
    @DisplayName("Deve tratar corretamente uma ConstraintViolationException")
    void handleConstraintViolationException() {
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(violation.getMessage()).thenReturn("campo inválido");
        when(violation.getPropertyPath()).thenReturn(path);
        when(path.toString()).thenReturn("nome");
        violations.add(violation);
        ConstraintViolationException ex = new ConstraintViolationException("Erro de validação", violations);
        ResponseEntity<Object> response = exceptionHandler.handleConstraintViolationException(ex, webRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Problem problem = (Problem) response.getBody();
        assertNotNull(problem);
        assertEquals(400, problem.getStatus());
    }

    @Test
    @DisplayName("Deve tratar corretamente uma MethodArgumentNotValidException")
    void handleMethodArgumentNotValid() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("object", "field", "mensagem de erro")
        ));
        ResponseEntity<Object> response = exceptionHandler.handleMethodArgumentNotValid(
                ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Problem problem = (Problem) response.getBody();
        assertNotNull(problem);
        assertEquals(400, problem.getStatus());
    }

    @Test
    @DisplayName("Deve tratar corretamente uma ClienteNaoEncontradoException")
    void handleClienteNaoEncontradoException() {
        ClienteNaoEncontradoException ex = new ClienteNaoEncontradoException("Cliente não encontrado");
        ResponseEntity<Object> response = exceptionHandler.handleEstadoNaoEncontradoException(ex, webRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Problem problem = (Problem) response.getBody();
        assertNotNull(problem);
        assertEquals(404, problem.getStatus());
    }

    @Test
    @DisplayName("Deve tratar corretamente uma CEPInvalidoException")
    void handleCEPInvalidoException() {        
        CEPInvalidoException ex = new CEPInvalidoException();
        ResponseEntity<Object> response = exceptionHandler.handleCEPInvalidoException(ex, webRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Problem problem = (Problem) response.getBody();
        assertNotNull(problem);
        assertEquals(409, problem.getStatus());
    }

    @Test
    @DisplayName("Deve tratar corretamente uma CPFInvalidoException")
    void handleCPFInvalidoException() {
        CPFInvalidoException ex = new CPFInvalidoException();
        ResponseEntity<Object> response = exceptionHandler.handleCPFInvalidoException(ex, webRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Problem problem = (Problem) response.getBody();
        assertNotNull(problem);
        assertEquals(409, problem.getStatus());
    }

    @Test
    @DisplayName("Deve tratar corretamente uma CpfJaCadastradoException")
    void handleCpfJaCadastradoException() {
        CpfJaCadastradoException ex = new CpfJaCadastradoException("CPF já cadastrado");
        ResponseEntity<Object> response = exceptionHandler.handleCPFInvalidoException(ex, webRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Problem problem = (Problem) response.getBody();
        assertNotNull(problem);
        assertEquals(409, problem.getStatus());
    }

    @Test
    @DisplayName("Deve tratar corretamente uma DataIntegrityViolationException")
    void handleDataIntegrityViolationException() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException(
                "Duplicate entry '123.456.789-10' for key 'cpf'"
        );
        ResponseEntity<Object> response = exceptionHandler.handleDataIntegrityViolationException(ex, webRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Problem problem = (Problem) response.getBody();
        assertNotNull(problem);
        assertEquals(409, problem.getStatus());
    }

    @Test
    @DisplayName("Deve tratar corretamente uma NoHandlerFoundException")
    void handleNoHandlerFoundException() {
        NoHandlerFoundException ex = new NoHandlerFoundException("GET", "/invalid-path", new HttpHeaders());
        ResponseEntity<Object> response = exceptionHandler.handleNoHandlerFoundException(
                ex, new HttpHeaders(), HttpStatus.NOT_FOUND, webRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Problem problem = (Problem) response.getBody();
        assertNotNull(problem);
        assertEquals(404, problem.getStatus());
    }
}