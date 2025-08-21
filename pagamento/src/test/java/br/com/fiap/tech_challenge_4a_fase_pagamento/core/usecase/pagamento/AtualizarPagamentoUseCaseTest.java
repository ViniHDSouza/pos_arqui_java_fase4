package br.com.fiap.tech_challenge_4a_fase_pagamento.core.usecase.pagamento;

import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.Pagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.gateways.PagamentoGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@DisplayName("Testes Unitários para AtualizarPagamentoUseCase")
class AtualizarPagamentoUseCaseTest {

    private PagamentoGateway pagamentoGateway;
    private AtualizarPagamentoUseCase atualizarPagamentoUseCase;

    @BeforeEach
    void setUp() {
        pagamentoGateway = Mockito.mock(PagamentoGateway.class);
        atualizarPagamentoUseCase = new AtualizarPagamentoUseCase(pagamentoGateway);
    }

    @Test
    @DisplayName("Deve chamar o método salvar do gateway ao atualizar um pagamento")
    void deveChamarSalvarAoAtualizarPagamento() {
        Pagamento pagamento = Mockito.mock(Pagamento.class);
        atualizarPagamentoUseCase.atualizar(pagamento);
        Mockito.verify(pagamentoGateway).salvar(pagamento);
    }
}