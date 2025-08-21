package br.com.fiap.tech_challenge_4a_fase_pagamento.core.usecase.pagamento;

import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.Pagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.exception.EstadoPagamentoInvalidoException;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.exception.FalhaProcessamentoPagamentoException;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.exception.PagamentoInvalidoException;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.exception.PagamentoNaoEncontradoException;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.gateways.PagamentoGateway;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.gateways.SistemaExternoPagamento;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EstornarPagamentoUseCase {

    private final PagamentoGateway pagamentoGateway;
    private final SistemaExternoPagamento sistemaExternoPagamento;

    public Pagamento estornar(Long pagamentoId) {
        if (pagamentoId == null || pagamentoId <= 0) {
            throw new PagamentoInvalidoException("ID do pagamento deve ser positivo");
        }

        Pagamento pagamento = pagamentoGateway.buscarPorId(pagamentoId)
                .orElseThrow(() -> new PagamentoNaoEncontradoException("Pagamento não encontrado"));

        if (!pagamento.isAprovado()) {
            throw new EstadoPagamentoInvalidoException("Só é possível estornar pagamentos aprovados");
        }

        try {
            boolean estornoSucesso = sistemaExternoPagamento.estornarPagamento(pagamento.getTransacaoExternaId());

            if (!estornoSucesso) {
                throw new FalhaProcessamentoPagamentoException("Falha ao estornar pagamento no sistema externo");
            }

            pagamento.estornar();

        } catch (Exception e) {
            throw new FalhaProcessamentoPagamentoException("Erro ao estornar pagamento: " + e.getMessage(), e);
        }

        return pagamentoGateway.salvar(pagamento);
    }

    public Pagamento estornarPorTransacaoExterna(String transacaoExternaId) {
        if (transacaoExternaId == null || transacaoExternaId.trim().isEmpty()) {
            throw new PagamentoInvalidoException("ID da transação externa é obrigatório");
        }

        Pagamento pagamento = pagamentoGateway.buscarPorTransacaoExternaId(transacaoExternaId)
                .orElseThrow(() -> new PagamentoNaoEncontradoException("Pagamento não encontrado para a transação externa"));

        return estornar(pagamento.getId());
    }
}
