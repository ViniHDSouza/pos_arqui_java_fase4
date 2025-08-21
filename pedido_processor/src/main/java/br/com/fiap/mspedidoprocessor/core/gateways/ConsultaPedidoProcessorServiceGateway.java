package br.com.fiap.mspedidoprocessor.core.gateways;

import br.com.fiap.mspedidoprocessor.core.domain.PagamentoCallback;

public interface ConsultaPedidoProcessorServiceGateway {

    PagamentoCallback consultarPagamentoCallback(Long pedidoId);

}
