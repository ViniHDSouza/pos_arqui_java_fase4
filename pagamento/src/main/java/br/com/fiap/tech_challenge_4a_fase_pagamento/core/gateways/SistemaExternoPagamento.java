package br.com.fiap.tech_challenge_4a_fase_pagamento.core.gateways;

import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.Pagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.ResultadoConsultaPagamento;

public interface SistemaExternoPagamento {


    String processarPagamento(Pagamento pagamento);
    ResultadoConsultaPagamento consultarStatusPagamento(String transacaoExternaId);
    boolean estornarPagamento(String transacaoExternaId);
}