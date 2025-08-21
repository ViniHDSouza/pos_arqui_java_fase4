package br.com.fiap.tech_challenge_4a_fase_pagamento.core.usecase.pagamento;


import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.Pagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.ResultadoConsultaPagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.exception.PagamentoInvalidoException;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.gateways.PagamentoGateway;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.gateways.SistemaExternoPagamento;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class ProcessarPagamentoUseCase {

    private final PagamentoGateway pagamentoGateway;
    private final SistemaExternoPagamento sistemaExternoPagamento;


    public Pagamento processar(Long pedidoId, Long clienteId, String numeroCartao, BigDecimal valor) {
        validarDadosPagamento(pedidoId, clienteId, numeroCartao, valor);

        Pagamento pagamento = new Pagamento(pedidoId, clienteId, numeroCartao, valor);

        pagamento = pagamentoGateway.salvar(pagamento);

        try {
            pagamento.iniciarProcessamento();
            pagamento = pagamentoGateway.salvar(pagamento);

            String transacaoExternaId = sistemaExternoPagamento.processarPagamento(pagamento);

            ResultadoConsultaPagamento resultado =
                    sistemaExternoPagamento.consultarStatusPagamento(transacaoExternaId);

            if (resultado.isAprovado()) {
                pagamento.aprovar(transacaoExternaId);
            } else {
                pagamento.rejeitar(resultado.getMotivo());
            }

        } catch (Exception e) {
            pagamento.marcarComoErro("Erro no processamento: " + e.getMessage());
        }

        return pagamentoGateway.salvar(pagamento);
    }

    private void validarDadosPagamento(Long pedidoId, Long clienteId, String numeroCartao, BigDecimal valor) {
        if (pedidoId == null || pedidoId <= 0) {
            throw new PagamentoInvalidoException("ID do pedido é obrigatório e deve ser positivo");
        }

        if (clienteId == null || clienteId <= 0) {
            throw new PagamentoInvalidoException("ID do cliente é obrigatório e deve ser positivo");
        }

        if (numeroCartao == null || numeroCartao.trim().isEmpty()) {
            throw new PagamentoInvalidoException("Número do cartão é obrigatório");
        }

        if (numeroCartao.replaceAll("\\D", "").length() < 13 ||
                numeroCartao.replaceAll("\\D", "").length() > 19) {
            throw new PagamentoInvalidoException("Número do cartão deve ter entre 13 e 19 dígitos");
        }

        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PagamentoInvalidoException("Valor deve ser positivo");
        }

        if (valor.scale() > 2) {
            throw new PagamentoInvalidoException("Valor não pode ter mais de 2 casas decimais");
        }
    }
}