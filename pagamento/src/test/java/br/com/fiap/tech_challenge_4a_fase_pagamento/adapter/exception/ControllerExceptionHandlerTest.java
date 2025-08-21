package br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.exception;

import br.com.fiap.tech_challenge_4a_fase_pagamento.core.exception.EstadoPagamentoInvalidoException;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.exception.PagamentoInvalidoException;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.exception.PagamentoNaoEncontradoException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Testes unitários para ControllerExceptionHandler")
class ControllerExceptionHandlerTest {
    private ControllerExceptionHandler handler;
    private WebRequest request;

    @BeforeEach
    void setUp() {
        handler = new ControllerExceptionHandler();
        request = mock(WebRequest.class);
    }

    @Test
    @DisplayName("Deve retornar 404 e mensagem adequada para PagamentoNaoEncontradoException")
    void deveTratarPagamentoNaoEncontradoException() {
        PagamentoNaoEncontradoException ex = new PagamentoNaoEncontradoException("Pagamento não encontrado");
        ResponseEntity<?> response = handler.handlePagamentoNaoEncontradoException(ex, request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Problem problem = (Problem) response.getBody();
        assertNotNull(problem);
        assertEquals("Pagamento não encontrado", problem.getDetail());
    }

    @Test
    @DisplayName("Deve retornar 404 e mensagem adequada para TransacaoNaoEncontradaException")
    void deveTratarTransacaoNaoEncontradaException() {
        TransacaoNaoEncontradaException ex = new TransacaoNaoEncontradaException("Transação não encontrada");
        ResponseEntity<?> response = handler.handleTransacaoNaoEncontradaException(ex, request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Problem problem = (Problem) response.getBody();
        assertNotNull(problem);
        assertTrue(problem.getDetail().contains("Transação não encontrada"));
    }

    @Test
    @DisplayName("Deve retornar 409 para EstadoPagamentoInvalidoException")
    void deveTratarEstadoPagamentoInvalidoException() {
        EstadoPagamentoInvalidoException ex = new EstadoPagamentoInvalidoException("Estado inválido");
        ResponseEntity<?> response = handler.handleEstadoPagamentoInvalidoException(ex, request);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Problem problem = (Problem) response.getBody();
        assertNotNull(problem);
        assertEquals("Estado inválido", problem.getDetail());
    }

    @Test
    @DisplayName("Deve retornar 400 para PagamentoInvalidoException")
    void deveTratarPagamentoInvalidoException() {
        PagamentoInvalidoException ex = new PagamentoInvalidoException("Dados inválidos");
        ResponseEntity<?> response = handler.handlePagamentoInvalidoException(ex, request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Problem problem = (Problem) response.getBody();
        assertNotNull(problem);
        assertEquals("Dados inválidos", problem.getDetail());
    }

    @Test
    @DisplayName("Deve retornar 409 para DataIntegrityViolationException com mensagem padrão")
    void deveTratarDataIntegrityViolationExceptionGenerica() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Erro de integridade");
        ResponseEntity<?> response = handler.handleDataIntegrityViolationException(ex, request);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Problem problem = (Problem) response.getBody();
        assertNotNull(problem);
        assertTrue(problem.getDetail().contains("integridade"));
    }

    @Test
    @DisplayName("Deve retornar 409 para DataIntegrityViolationException de CPF duplicado")
    void deveTratarDataIntegrityViolationExceptionCpfDuplicado() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Duplicate entry '12345678900' for key 'cpf'");
        ResponseEntity<?> response = handler.handleDataIntegrityViolationException(ex, request);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Problem problem = (Problem) response.getBody();
        assertNotNull(problem);
        assertTrue(problem.getUserMessage().contains("CPF já cadastrado"));
    }

    @Test
    @DisplayName("Deve retornar 400 e detalhes dos campos inválidos para ConstraintViolationException")
    void deveTratarConstraintViolationException() {
        
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("campoTeste");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("Campo obrigatório");
        Set<ConstraintViolation<?>> violations = Set.of(violation);
        ConstraintViolationException ex = new ConstraintViolationException(violations);

        ResponseEntity<?> response = handler.handleConstraintViolationException(ex, request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Problem problem = (Problem) response.getBody();
        assertNotNull(problem);
        assertEquals("Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.", problem.getDetail());
        assertNotNull(problem.getFields());
        assertEquals(1, problem.getFields().size());
        assertEquals("campoTeste", problem.getFields().get(0).getName());
        assertEquals("Campo obrigatório", problem.getFields().get(0).getUserMessage());
    }

    @Test
    @DisplayName("Deve retornar 400 e mensagem adequada para RequisicaoInvalidaException")
    void deveTratarRequisicaoInvalidaException() {
        
        RequisicaoInvalidaException ex = new RequisicaoInvalidaException("Requisição inválida: campo obrigatório ausente");
        ResponseEntity<?> response = handler.handleRequisicaoInvalidaException(ex, request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Problem problem = (Problem) response.getBody();
        assertNotNull(problem);
        assertEquals("Requisição inválida: campo obrigatório ausente", problem.getDetail());
        assertEquals("Requisição inválida: campo obrigatório ausente", problem.getUserMessage());
    }
}