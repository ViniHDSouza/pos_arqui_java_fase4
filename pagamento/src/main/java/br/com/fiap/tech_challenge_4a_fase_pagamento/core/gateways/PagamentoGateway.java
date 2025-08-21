package br.com.fiap.tech_challenge_4a_fase_pagamento.core.gateways;

import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.Pagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.StatusPagamento;

import java.util.List;
import java.util.Optional;

public interface  PagamentoGateway {

    Pagamento salvar(Pagamento pagamento);
    Optional<Pagamento> buscarPorId(Long id);
    List<Pagamento> buscarPorPedidoId(Long pedidoId);
    List<Pagamento> buscarPorClienteId(Long clienteId);
    List<Pagamento> buscarPorStatus(StatusPagamento status);
    Optional<Pagamento> buscarPorTransacaoExternaId(String transacaoExternaId);
    List<Pagamento> listarTodos();
}
