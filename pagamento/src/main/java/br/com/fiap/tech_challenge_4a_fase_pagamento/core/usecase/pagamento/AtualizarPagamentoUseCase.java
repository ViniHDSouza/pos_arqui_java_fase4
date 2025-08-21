package br.com.fiap.tech_challenge_4a_fase_pagamento.core.usecase.pagamento;

import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.Pagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.gateways.PagamentoGateway;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AtualizarPagamentoUseCase {

    private final PagamentoGateway pagamentoGateway;

    public void atualizar(Pagamento pagamento) {
        pagamentoGateway.salvar(pagamento);
    }

}
