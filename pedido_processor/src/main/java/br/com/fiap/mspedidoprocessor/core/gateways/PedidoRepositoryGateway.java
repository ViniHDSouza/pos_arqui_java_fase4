package br.com.fiap.mspedidoprocessor.core.gateways;

import br.com.fiap.mspedidoprocessor.core.domain.PedidoProcessor;

public interface PedidoRepositoryGateway {
    PedidoProcessor salvar(PedidoProcessor pedidoProcessor);
}