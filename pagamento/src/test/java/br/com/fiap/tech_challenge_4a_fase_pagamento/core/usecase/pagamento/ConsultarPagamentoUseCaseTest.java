package br.com.fiap.tech_challenge_4a_fase_pagamento.core.usecase.pagamento;

import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.Pagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.StatusPagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.exception.PagamentoInvalidoException;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.exception.PagamentoNaoEncontradoException;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.gateways.PagamentoGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ActiveProfiles("test")
@DisplayName("Testes Unitários para ConsultarPagamentoUseCase")
class ConsultarPagamentoUseCaseTest {
    private PagamentoGateway pagamentoGateway;
    private ConsultarPagamentoUseCase consultarPagamentoUseCase;

    @BeforeEach
    void setUp() {
        pagamentoGateway = Mockito.mock(PagamentoGateway.class);
        consultarPagamentoUseCase = new ConsultarPagamentoUseCase(pagamentoGateway);
    }

    @Test
    @DisplayName("Deve retornar o pagamento ao buscar por ID válido existente")
    void deveBuscarPagamentoPorIdComSucesso() {
        Pagamento pagamento = Mockito.mock(Pagamento.class);
        Mockito.when(pagamentoGateway.buscarPorId(1L)).thenReturn(Optional.of(pagamento));
        Optional<Pagamento> resultado = consultarPagamentoUseCase.buscarPorId(1L);
        assertTrue(resultado.isPresent());
        assertEquals(pagamento, resultado.get());
    }

    @Test
    @DisplayName("Deve lançar PagamentoInvalidoException ao buscar por ID nulo ou menor/igual a zero")
    void deveLancarExcecaoQuandoIdInvalido() {
        assertThrows(PagamentoInvalidoException.class, () -> consultarPagamentoUseCase.buscarPorId(0L));
        assertThrows(PagamentoInvalidoException.class, () -> consultarPagamentoUseCase.buscarPorId(null));
    }

    @Test
    @DisplayName("Deve lançar PagamentoNaoEncontradoException ao buscar por ID inexistente")
    void deveLancarExcecaoQuandoPagamentoNaoEncontrado() {
        Mockito.when(pagamentoGateway.buscarPorId(2L)).thenReturn(Optional.empty());
        assertThrows(PagamentoNaoEncontradoException.class, () -> consultarPagamentoUseCase.buscarPorId(2L));
    }

    @Test
    @DisplayName("Deve retornar lista de pagamentos ao buscar por pedidoId válido existente")
    void deveBuscarPorPedidoIdComSucesso() {
        Pagamento pagamento = Mockito.mock(Pagamento.class);
        Mockito.when(pagamentoGateway.buscarPorPedidoId(10L)).thenReturn(List.of(pagamento));
        List<Pagamento> resultado = consultarPagamentoUseCase.buscarPorPedidoId(10L);
        assertEquals(1, resultado.size());
        assertEquals(pagamento, resultado.get(0));
    }

    @Test
    @DisplayName("Deve lançar PagamentoInvalidoException ao buscar por pedidoId nulo ou menor/igual a zero")
    void deveLancarExcecaoQuandoPedidoIdInvalido() {
        assertThrows(PagamentoInvalidoException.class, () -> consultarPagamentoUseCase.buscarPorPedidoId(0L));
        assertThrows(PagamentoInvalidoException.class, () -> consultarPagamentoUseCase.buscarPorPedidoId(null));
    }

    @Test
    @DisplayName("Deve lançar PagamentoNaoEncontradoException ao buscar por pedidoId inexistente")
    void deveLancarExcecaoQuandoPedidoIdNaoEncontrado() {
        Mockito.when(pagamentoGateway.buscarPorPedidoId(20L)).thenReturn(Collections.emptyList());
        assertThrows(PagamentoNaoEncontradoException.class, () -> consultarPagamentoUseCase.buscarPorPedidoId(20L));
    }

    @Test
    @DisplayName("Deve retornar lista de pagamentos ao buscar por clienteId válido existente")
    void deveBuscarPorClienteIdComSucesso() {
        Pagamento pagamento = Mockito.mock(Pagamento.class);
        Mockito.when(pagamentoGateway.buscarPorClienteId(30L)).thenReturn(List.of(pagamento));
        List<Pagamento> resultado = consultarPagamentoUseCase.buscarPorClienteId(30L);
        assertEquals(1, resultado.size());
        assertEquals(pagamento, resultado.get(0));
    }

    @Test
    @DisplayName("Deve lançar PagamentoInvalidoException ao buscar por clienteId nulo ou menor/igual a zero")
    void deveLancarExcecaoQuandoClienteIdInvalido() {
        assertThrows(PagamentoInvalidoException.class, () -> consultarPagamentoUseCase.buscarPorClienteId(0L));
        assertThrows(PagamentoInvalidoException.class, () -> consultarPagamentoUseCase.buscarPorClienteId(null));
    }

    @Test
    @DisplayName("Deve retornar lista de pagamentos ao buscar por status válido existente")
    void deveBuscarPorStatusComSucesso() {
        Pagamento pagamento = Mockito.mock(Pagamento.class);
        Mockito.when(pagamentoGateway.buscarPorStatus(StatusPagamento.APROVADO)).thenReturn(List.of(pagamento));
        List<Pagamento> resultado = consultarPagamentoUseCase.buscarPorStatus(StatusPagamento.APROVADO);
        assertEquals(1, resultado.size());
        assertEquals(pagamento, resultado.get(0));
    }

    @Test
    @DisplayName("Deve lançar PagamentoInvalidoException ao buscar por status nulo")
    void deveLancarExcecaoQuandoStatusNulo() {
        assertThrows(PagamentoInvalidoException.class, () -> consultarPagamentoUseCase.buscarPorStatus(null));
    }

    @Test
    @DisplayName("Deve lançar PagamentoNaoEncontradoException ao buscar por status inexistente")
    void deveLancarExcecaoQuandoStatusNaoEncontrado() {
        Mockito.when(pagamentoGateway.buscarPorStatus(StatusPagamento.REJEITADO)).thenReturn(Collections.emptyList());
        assertThrows(PagamentoNaoEncontradoException.class, () -> consultarPagamentoUseCase.buscarPorStatus(StatusPagamento.REJEITADO));
    }

    @Test
    @DisplayName("Deve retornar lista de todos os pagamentos ao chamar listarTodos")
    void deveListarTodosOsPagamentos() {
        Pagamento pagamento = Mockito.mock(Pagamento.class);
        Mockito.when(pagamentoGateway.listarTodos()).thenReturn(List.of(pagamento));
        List<Pagamento> resultado = consultarPagamentoUseCase.listarTodos();
        assertEquals(1, resultado.size());
        assertEquals(pagamento, resultado.get(0));
    }

    @Test
    @DisplayName("Deve retornar pagamento ao buscar por transacaoExternaId válido")
    void deveBuscarPorTransacaoExternaIdComSucesso() {
        Pagamento pagamento = Mockito.mock(Pagamento.class);
        Mockito.when(pagamentoGateway.buscarPorTransacaoExternaId("TXN123")).thenReturn(Optional.of(pagamento));
        Optional<Pagamento> resultado = consultarPagamentoUseCase.buscarPorTransacaoExternaId("TXN123");
        assertTrue(resultado.isPresent());
        assertEquals(pagamento, resultado.get());
    }

    @Test
    @DisplayName("Deve lançar PagamentoInvalidoException ao buscar por transacaoExternaId nulo ou vazio")
    void deveLancarExcecaoQuandoTransacaoExternaIdInvalido() {
        assertThrows(PagamentoInvalidoException.class, () -> consultarPagamentoUseCase.buscarPorTransacaoExternaId(null));
        assertThrows(PagamentoInvalidoException.class, () -> consultarPagamentoUseCase.buscarPorTransacaoExternaId(" "));
    }
}