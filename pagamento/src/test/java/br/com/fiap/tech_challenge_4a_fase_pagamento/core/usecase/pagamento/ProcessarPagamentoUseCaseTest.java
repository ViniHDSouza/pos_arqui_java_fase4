package br.com.fiap.tech_challenge_4a_fase_pagamento.core.usecase.pagamento;

import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.Pagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.ResultadoConsultaPagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.gateways.PagamentoGateway;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.gateways.SistemaExternoPagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@ActiveProfiles("test")
@DisplayName("Testes Unitários para ProcessarPagamentoUseCase")
class ProcessarPagamentoUseCaseTest {
    private PagamentoGateway pagamentoGateway;
    private SistemaExternoPagamento sistemaExternoPagamento;
    private ProcessarPagamentoUseCase processarPagamentoUseCase;

    @BeforeEach
    void setUp() {
        pagamentoGateway = Mockito.mock(PagamentoGateway.class);
        sistemaExternoPagamento = Mockito.mock(SistemaExternoPagamento.class);
        processarPagamentoUseCase = new ProcessarPagamentoUseCase(pagamentoGateway, sistemaExternoPagamento);
    }

    @Test
    @DisplayName("Deve processar pagamento com dados válidos e aprovar quando o sistema externo aprova")
    void deveProcessarPagamentoAprovado() {
        Pagamento pagamento = Mockito.mock(Pagamento.class);
        Mockito.when(pagamentoGateway.salvar(Mockito.any())).thenReturn(pagamento);
        Mockito.doNothing().when(pagamento).iniciarProcessamento();
        Mockito.doNothing().when(pagamento).aprovar(Mockito.anyString());
        Mockito.doNothing().when(pagamento).rejeitar(Mockito.anyString());
        Mockito.doNothing().when(pagamento).marcarComoErro(Mockito.anyString());
        Mockito.when(sistemaExternoPagamento.processarPagamento(pagamento)).thenReturn("TXN_OK");
        ResultadoConsultaPagamento resultado = Mockito.mock(ResultadoConsultaPagamento.class);
        Mockito.when(resultado.isAprovado()).thenReturn(true);
        Mockito.when(sistemaExternoPagamento.consultarStatusPagamento("TXN_OK")).thenReturn(resultado);

        Pagamento resultadoPagamento = processarPagamentoUseCase.processar(1L, 2L, "1234567890123", new BigDecimal("10.00"));
        assertNotNull(resultadoPagamento);
        Mockito.verify(pagamento).aprovar("TXN_OK");
    }

    @Test
    @DisplayName("Deve processar pagamento com dados válidos e rejeitar quando o sistema externo rejeita")
    void deveProcessarPagamentoRejeitado() {
        Pagamento pagamento = Mockito.mock(Pagamento.class);
        Mockito.when(pagamentoGateway.salvar(Mockito.any())).thenReturn(pagamento);
        Mockito.doNothing().when(pagamento).iniciarProcessamento();
        Mockito.doNothing().when(pagamento).aprovar(Mockito.anyString());
        Mockito.doNothing().when(pagamento).rejeitar(Mockito.anyString());
        Mockito.doNothing().when(pagamento).marcarComoErro(Mockito.anyString());
        Mockito.when(sistemaExternoPagamento.processarPagamento(pagamento)).thenReturn("TXN_REJ");
        ResultadoConsultaPagamento resultado = Mockito.mock(ResultadoConsultaPagamento.class);
        Mockito.when(resultado.isAprovado()).thenReturn(false);
        Mockito.when(resultado.getMotivo()).thenReturn("Cartão recusado");
        Mockito.when(sistemaExternoPagamento.consultarStatusPagamento("TXN_REJ")).thenReturn(resultado);

        Pagamento resultadoPagamento = processarPagamentoUseCase.processar(1L, 2L, "1234567890123", new BigDecimal("10.00"));
        assertNotNull(resultadoPagamento);
        Mockito.verify(pagamento).rejeitar("Cartão recusado");
    }

    @Test
    @DisplayName("Deve marcar pagamento com erro ao ocorrer exceção durante processamento")
    void deveMarcarPagamentoComoErroEmExcecao() {
        Pagamento pagamento = Mockito.mock(Pagamento.class);
        Mockito.when(pagamentoGateway.salvar(Mockito.any())).thenReturn(pagamento);
        Mockito.doThrow(new RuntimeException("Falha externa")).when(sistemaExternoPagamento).processarPagamento(pagamento);
        Mockito.doNothing().when(pagamento).iniciarProcessamento();
        Mockito.doNothing().when(pagamento).marcarComoErro(Mockito.anyString());

        Pagamento resultadoPagamento = processarPagamentoUseCase.processar(1L, 2L, "1234567890123", new BigDecimal("10.00"));
        assertNotNull(resultadoPagamento);
        Mockito.verify(pagamento).marcarComoErro(Mockito.contains("Falha externa"));
    }

}
