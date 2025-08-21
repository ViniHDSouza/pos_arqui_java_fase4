package br.com.fiap.tech_challenge_4a_fase_pagamento.core.usecase.pagamento;


import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.Pagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.StatusPagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.exception.PagamentoInvalidoException;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.exception.PagamentoNaoEncontradoException;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.gateways.PagamentoGateway;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class ConsultarPagamentoUseCase {

    private final PagamentoGateway pagamentoGateway;

    public Optional<Pagamento> buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new PagamentoInvalidoException("ID deve ser positivo");
        }
        Optional<Pagamento> pagamento = pagamentoGateway.buscarPorId(id);
        if (pagamento.isEmpty()) {
            throw new PagamentoNaoEncontradoException("Pagamento não encontrado para o ID: " + id);
        }
        return pagamento;
    }

    public List<Pagamento> buscarPorPedidoId(Long pedidoId) {
        if (pedidoId == null || pedidoId <= 0) {
            throw new PagamentoInvalidoException("ID do pedido deve ser positivo");
        }
        List<Pagamento> pagamentos = pagamentoGateway.buscarPorPedidoId(pedidoId);
        if (pagamentos.isEmpty()) {
            throw new PagamentoNaoEncontradoException("Nenhum pagamento encontrado para o Pedido ID: " + pedidoId);
        }
        return pagamentos;
    }

    public List<Pagamento> buscarPorClienteId(Long clienteId) {
        if (clienteId == null || clienteId <= 0) {
            throw new PagamentoInvalidoException("ID do cliente deve ser positivo");
        }
        return pagamentoGateway.buscarPorClienteId(clienteId);
    }

    public List<Pagamento> buscarPorStatus(StatusPagamento status) {
        if (status == null) {
            throw new PagamentoInvalidoException("Status é obrigatório");
        }
        List<Pagamento> pagamentos = pagamentoGateway.buscarPorStatus(status);
        if (pagamentos.isEmpty()) {
            throw new PagamentoNaoEncontradoException("Nenhum pagamento encontrado com o status: " + status);
        }
        return pagamentos;
    }

    public List<Pagamento> listarTodos() {
        return pagamentoGateway.listarTodos();
    }

    public Optional<Pagamento> buscarPorTransacaoExternaId(String transacaoExternaId) {
        if (transacaoExternaId == null || transacaoExternaId.trim().isEmpty()) {
            throw new PagamentoInvalidoException("ID da transação externa é obrigatório");
        }
        return pagamentoGateway.buscarPorTransacaoExternaId(transacaoExternaId);
    }
}