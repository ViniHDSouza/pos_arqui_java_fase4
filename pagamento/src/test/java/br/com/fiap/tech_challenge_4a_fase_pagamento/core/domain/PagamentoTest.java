package br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain;

import br.com.fiap.tech_challenge_4a_fase_pagamento.core.exception.EstadoPagamentoInvalidoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para Pagamento (domain)")
class PagamentoTest {

    @Test
    @DisplayName("Deve criar Pagamento com construtor de novos pagamentos e status PENDENTE")
    void deveCriarPagamentoNovoComStatusPendente() {
        Pagamento pagamento = new Pagamento(1L, 2L, "1234", new BigDecimal("10.00"));
        assertEquals(1L, pagamento.getPedidoId());
        assertEquals(2L, pagamento.getClienteId());
        assertEquals("1234", pagamento.getNumeroCartao());
        assertEquals(new BigDecimal("10.00"), pagamento.getValor());
        assertEquals(StatusPagamento.PENDENTE, pagamento.getStatus());
        assertNotNull(pagamento.getDataCriacao());
        assertNotNull(pagamento.getDataAtualizacao());
    }

    @Test
    @DisplayName("Deve criar Pagamento com todos os campos pelo construtor completo")
    void deveCriarPagamentoComConstrutorCompleto() {
        LocalDateTime agora = LocalDateTime.now();
        Pagamento pagamento = new Pagamento(10L, 20L, 30L, "9999", new BigDecimal("99.99"), StatusPagamento.APROVADO, "tx-1", "motivo", agora, agora);
        assertEquals(10L, pagamento.getId());
        assertEquals(20L, pagamento.getPedidoId());
        assertEquals(30L, pagamento.getClienteId());
        assertEquals("9999", pagamento.getNumeroCartao());
        assertEquals(new BigDecimal("99.99"), pagamento.getValor());
        assertEquals(StatusPagamento.APROVADO, pagamento.getStatus());
        assertEquals("tx-1", pagamento.getTransacaoExternaId());
        assertEquals("motivo", pagamento.getMotivoRejeicao());
        assertEquals(agora, pagamento.getDataCriacao());
        assertEquals(agora, pagamento.getDataAtualizacao());
    }

    @Test
    @DisplayName("Deve iniciar processamento apenas se status for PENDENTE")
    void deveIniciarProcessamentoComStatusPendente() {
        Pagamento pagamento = Pagamento.builder().status(StatusPagamento.PENDENTE).build();
        pagamento.iniciarProcessamento();
        assertEquals(StatusPagamento.PROCESSANDO, pagamento.getStatus());
    }

    @Test
    @DisplayName("Deve lançar exceção ao iniciar processamento se status não for PENDENTE")
    void deveLancarExcecaoAoIniciarProcessamentoComStatusInvalido() {
        Pagamento pagamento = Pagamento.builder().status(StatusPagamento.APROVADO).build();
        assertThrows(EstadoPagamentoInvalidoException.class, pagamento::iniciarProcessamento);
    }

    @Test
    @DisplayName("Deve aprovar pagamento apenas se status for PROCESSANDO")
    void deveAprovarPagamentoComStatusProcessando() {
        Pagamento pagamento = Pagamento.builder().status(StatusPagamento.PROCESSANDO).build();
        pagamento.aprovar("tx-123");
        assertEquals(StatusPagamento.APROVADO, pagamento.getStatus());
        assertEquals("tx-123", pagamento.getTransacaoExternaId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao aprovar se status não for PROCESSANDO")
    void deveLancarExcecaoAoAprovarComStatusInvalido() {
        Pagamento pagamento = Pagamento.builder().status(StatusPagamento.PENDENTE).build();
        assertThrows(EstadoPagamentoInvalidoException.class, () -> pagamento.aprovar("tx"));
    }

    @Test
    @DisplayName("Deve rejeitar pagamento apenas se status for PROCESSANDO")
    void deveRejeitarPagamentoComStatusProcessando() {
        Pagamento pagamento = Pagamento.builder().status(StatusPagamento.PROCESSANDO).build();
        pagamento.rejeitar("cartão bloqueado");
        assertEquals(StatusPagamento.REJEITADO, pagamento.getStatus());
        assertEquals("cartão bloqueado", pagamento.getMotivoRejeicao());
    }

    @Test
    @DisplayName("Deve lançar exceção ao rejeitar se status não for PROCESSANDO")
    void deveLancarExcecaoAoRejeitarComStatusInvalido() {
        Pagamento pagamento = Pagamento.builder().status(StatusPagamento.APROVADO).build();
        assertThrows(EstadoPagamentoInvalidoException.class, () -> pagamento.rejeitar("motivo"));
    }

    @Test
    @DisplayName("Deve marcar como erro e atualizar status e motivo")
    void deveMarcarComoErro() {
        Pagamento pagamento = Pagamento.builder().status(StatusPagamento.PENDENTE).build();
        pagamento.marcarComoErro("erro externo");
        assertEquals(StatusPagamento.ERRO, pagamento.getStatus());
        assertEquals("erro externo", pagamento.getMotivoRejeicao());
    }

    @Test
    @DisplayName("Deve estornar pagamento apenas se status for APROVADO")
    void deveEstornarPagamentoComStatusAprovado() {
        Pagamento pagamento = Pagamento.builder().status(StatusPagamento.APROVADO).build();
        pagamento.estornar();
        assertEquals(StatusPagamento.ESTORNADO, pagamento.getStatus());
    }

    @Test
    @DisplayName("Deve lançar exceção ao estornar se status não for APROVADO")
    void deveLancarExcecaoAoEstornarComStatusInvalido() {
        Pagamento pagamento = Pagamento.builder().status(StatusPagamento.PENDENTE).build();
        assertThrows(EstadoPagamentoInvalidoException.class, pagamento::estornar);
    }

    @Test
    @DisplayName("Deve identificar corretamente status de aprovado, rejeitado e processando")
    void deveIdentificarStatusConveniencia() {
        Pagamento aprovado = Pagamento.builder().status(StatusPagamento.APROVADO).build();
        Pagamento rejeitado = Pagamento.builder().status(StatusPagamento.REJEITADO).build();
        Pagamento processando = Pagamento.builder().status(StatusPagamento.PROCESSANDO).build();
        assertTrue(aprovado.isAprovado());
        assertFalse(aprovado.isRejeitado());
        assertFalse(aprovado.isProcessando());
        assertTrue(rejeitado.isRejeitado());
        assertFalse(rejeitado.isAprovado());
        assertFalse(rejeitado.isProcessando());
        assertTrue(processando.isProcessando());
        assertFalse(processando.isAprovado());
        assertFalse(processando.isRejeitado());
    }
}
