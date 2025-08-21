package br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.external;

import br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.exception.TransacaoNaoEncontradaException;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.Pagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.ResultadoConsultaPagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para SistemaExternoPagamentoMock")
class SistemaExternoPagamentoMockTest {
    private SistemaExternoPagamentoMock sistemaExterno;

    @BeforeEach
    void setUp() {
        sistemaExterno = new SistemaExternoPagamentoMock();
    }

    @Test
    @DisplayName("Deve processar pagamento e registrar transação aprovada ou rejeitada")
    void deveProcessarPagamento() {
        Pagamento pagamento = new Pagamento(1L, 1L, "1234567890123", new BigDecimal("100.00"));
        String transacaoId = sistemaExterno.processarPagamento(pagamento);
        assertNotNull(transacaoId);
        ResultadoConsultaPagamento resultado = sistemaExterno.consultarStatusPagamento(transacaoId);
        assertNotNull(resultado);
        assertTrue(resultado.getStatus().equals("APROVADO") || resultado.getStatus().equals("REJEITADO"));
    }

    @Test
    @DisplayName("Deve consultar status de transação inexistente e retornar NAO_ENCONTRADO")
    void deveRetornarNaoEncontradoParaTransacaoInexistente() {
        ResultadoConsultaPagamento resultado = sistemaExterno.consultarStatusPagamento("INEXISTENTE");
        assertEquals("NAO_ENCONTRADO", resultado.getStatus());
        assertFalse(resultado.isAprovado());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar estornar transação inexistente")
    void deveLancarExcecaoAoEstornarTransacaoInexistente() {
        assertThrows(TransacaoNaoEncontradaException.class, () -> sistemaExterno.estornarPagamento("NAO_EXISTE"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar estornar transação não aprovada")
    void deveLancarExcecaoAoEstornarTransacaoNaoAprovada() {
        Pagamento pagamento = new Pagamento(1L, 1L, "1234567890000", new BigDecimal("100.00")); // termina com 0000, sempre rejeita
        String transacaoId = sistemaExterno.processarPagamento(pagamento);
        assertThrows(TransacaoNaoEncontradaException.class, () -> sistemaExterno.estornarPagamento(transacaoId));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar estornar transação já estornada, garantindo isolamento e determinismo")
    void deveLancarExcecaoAoEstornarTransacaoJaEstornada() {
        String transacaoIdAprovada;
        Pagamento pagamento;
        ResultadoConsultaPagamento resultado;
        do {
            pagamento = new Pagamento(1L, 1L, "1234567890123", new BigDecimal("100.00"));
            transacaoIdAprovada = sistemaExterno.processarPagamento(pagamento);
            resultado = sistemaExterno.consultarStatusPagamento(transacaoIdAprovada);
        } while (!resultado.isAprovado());

        sistemaExterno.estornarPagamento(transacaoIdAprovada);
        final String transacaoFinal = transacaoIdAprovada;
        assertThrows(TransacaoNaoEncontradaException.class, () -> sistemaExterno.estornarPagamento(transacaoFinal));
    }
    
    @Test
    @DisplayName("Deve consultar status de transação existente")
    void deveConsultarStatusTransacaoExistente() {
        Pagamento pagamento = new Pagamento(1L, 1L, "123456", new BigDecimal("100.00"));
        String idTransacao = sistemaExterno.processarPagamento(pagamento);

        ResultadoConsultaPagamento resultado = sistemaExterno.consultarStatusPagamento(idTransacao);

        assertNotNull(resultado);
        assertNotNull(resultado.getStatus());
    }

    @Test
    @DisplayName("Deve retornar resultado NAO_ENCONTRADO ao consultar transação inexistente")
    void deveRetornarNaoEncontradoQuandoTransacaoNaoExiste() {
        String idFalso = "";

        ResultadoConsultaPagamento resultado = sistemaExterno.consultarStatusPagamento(idFalso);

        assertEquals("NAO_ENCONTRADO", resultado.getStatus());
        assertEquals("Transação não encontrada", resultado.getMotivo());
        assertFalse(resultado.isAprovado());
    }
}