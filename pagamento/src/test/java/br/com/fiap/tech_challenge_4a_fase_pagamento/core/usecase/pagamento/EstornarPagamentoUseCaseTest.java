package br.com.fiap.tech_challenge_4a_fase_pagamento.core.usecase.pagamento;

import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.Pagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.exception.EstadoPagamentoInvalidoException;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.exception.FalhaProcessamentoPagamentoException;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.exception.PagamentoInvalidoException;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.exception.PagamentoNaoEncontradoException;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.gateways.PagamentoGateway;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.gateways.SistemaExternoPagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
@ActiveProfiles("test")
@DisplayName("Testes unitários para EstornarPagamentoUseCase")
class EstornarPagamentoUseCaseTest {
    private PagamentoGateway pagamentoGateway;
    private SistemaExternoPagamento sistemaExternoPagamento;
    private EstornarPagamentoUseCase estornarPagamentoUseCase;

    @BeforeEach
    void setUp() {
        pagamentoGateway = mock(PagamentoGateway.class);
        sistemaExternoPagamento = mock(SistemaExternoPagamento.class);
        estornarPagamentoUseCase = new EstornarPagamentoUseCase(pagamentoGateway, sistemaExternoPagamento);
    }

    @Test
    @DisplayName("Deve lançar PagamentoInvalidoException quando o ID do pagamento for nulo ou menor/igual a zero")
    void deveLancarExcecaoParaIdInvalido() {
        assertThrows(PagamentoInvalidoException.class, () -> estornarPagamentoUseCase.estornar(null));
        assertThrows(PagamentoInvalidoException.class, () -> estornarPagamentoUseCase.estornar(0L));
        assertThrows(PagamentoInvalidoException.class, () -> estornarPagamentoUseCase.estornar(-1L));
    }

    @Test
    @DisplayName("Deve lançar PagamentoNaoEncontradoException quando o pagamento não for encontrado pelo ID")
    void deveLancarExcecaoQuandoPagamentoNaoEncontrado() {
        when(pagamentoGateway.buscarPorId(10L)).thenReturn(Optional.empty());
        assertThrows(PagamentoNaoEncontradoException.class, () -> estornarPagamentoUseCase.estornar(10L));
    }

    @Test
    @DisplayName("Deve lançar EstadoPagamentoInvalidoException quando o pagamento não estiver aprovado")
    void deveLancarExcecaoQuandoPagamentoNaoAprovado() {
        Pagamento pagamento = mock(Pagamento.class);
        when(pagamento.isAprovado()).thenReturn(false);
        when(pagamentoGateway.buscarPorId(1L)).thenReturn(Optional.of(pagamento));
        assertThrows(EstadoPagamentoInvalidoException.class, () -> estornarPagamentoUseCase.estornar(1L));
    }

    @Test
    @DisplayName("Deve lançar FalhaProcessamentoPagamentoException quando o sistema externo retorna falso para estorno")
    void deveLancarExcecaoQuandoEstornoExternoFalha() {
        Pagamento pagamento = mock(Pagamento.class);
        when(pagamento.isAprovado()).thenReturn(true);
        when(pagamento.getTransacaoExternaId()).thenReturn("TXN123");
        when(pagamentoGateway.buscarPorId(1L)).thenReturn(Optional.of(pagamento));
        when(sistemaExternoPagamento.estornarPagamento("TXN123")).thenReturn(false);
        assertThrows(FalhaProcessamentoPagamentoException.class, () -> estornarPagamentoUseCase.estornar(1L));
    }

    @Test
    @DisplayName("Deve lançar FalhaProcessamentoPagamentoException quando ocorre exceção no sistema externo")
    void deveLancarExcecaoQuandoEstornoExternoLancaExcecao() {
        Pagamento pagamento = mock(Pagamento.class);
        when(pagamento.isAprovado()).thenReturn(true);
        when(pagamento.getTransacaoExternaId()).thenReturn("TXN123");
        when(pagamentoGateway.buscarPorId(1L)).thenReturn(Optional.of(pagamento));
        when(sistemaExternoPagamento.estornarPagamento("TXN123")).thenThrow(new RuntimeException("Falha externa"));
        assertThrows(FalhaProcessamentoPagamentoException.class, () -> estornarPagamentoUseCase.estornar(1L));
    }

    @Test
    @DisplayName("Deve estornar pagamento aprovado com sucesso e salvar o pagamento atualizado")
    void deveEstornarPagamentoComSucesso() {
        Pagamento pagamento = mock(Pagamento.class);
        when(pagamento.isAprovado()).thenReturn(true);
        when(pagamento.getTransacaoExternaId()).thenReturn("TXN123");
        when(pagamentoGateway.buscarPorId(1L)).thenReturn(Optional.of(pagamento));
        when(sistemaExternoPagamento.estornarPagamento("TXN123")).thenReturn(true);
        when(pagamentoGateway.salvar(pagamento)).thenReturn(pagamento);

        Pagamento resultado = estornarPagamentoUseCase.estornar(1L);
        assertNotNull(resultado);
        verify(pagamento).estornar();
        verify(pagamentoGateway).salvar(pagamento);
    }

    @Test
    @DisplayName("Deve lançar PagamentoInvalidoException quando transacaoExternaId for nulo ou vazio no estorno por transação externa")
    void deveLancarExcecaoParaTransacaoExternaIdInvalido() {
        assertThrows(PagamentoInvalidoException.class, () -> estornarPagamentoUseCase.estornarPorTransacaoExterna(null));
        assertThrows(PagamentoInvalidoException.class, () -> estornarPagamentoUseCase.estornarPorTransacaoExterna(" "));
    }

    @Test
    @DisplayName("Deve lançar PagamentoNaoEncontradoException quando não encontrar pagamento por transacaoExternaId")
    void deveLancarExcecaoQuandoNaoEncontrarPorTransacaoExternaId() {
        when(pagamentoGateway.buscarPorTransacaoExternaId("NAOEXISTE")).thenReturn(Optional.empty());
        assertThrows(PagamentoNaoEncontradoException.class, () -> estornarPagamentoUseCase.estornarPorTransacaoExterna("NAOEXISTE"));
    }

    @Test
    @DisplayName("Deve estornar pagamento com sucesso via transacaoExternaId")
    void deveEstornarPagamentoPorTransacaoExternaComSucesso() {
        Pagamento pagamento = mock(Pagamento.class);
        when(pagamento.getId()).thenReturn(99L);
        when(pagamentoGateway.buscarPorTransacaoExternaId("TXN_OK")).thenReturn(Optional.of(pagamento));
        EstornarPagamentoUseCase spyUseCase = Mockito.spy(estornarPagamentoUseCase);
        doReturn(pagamento).when(spyUseCase).estornar(99L);
        Pagamento resultado = spyUseCase.estornarPorTransacaoExterna("TXN_OK");
        assertNotNull(resultado);
        verify(spyUseCase).estornar(99L);
    }
}