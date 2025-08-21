package br.com.fiap.mspedidoprocessor.core.gateways;

import br.com.fiap.mspedidoprocessor.adapter.external.pagamentoservice.dto.PagamentoRequestDTO;
import br.com.fiap.mspedidoprocessor.adapter.external.pagamentoservice.dto.PagamentoResponseDTO;
import br.com.fiap.mspedidoprocessor.core.domain.PagamentoDTO;

public interface PagamentoServiceGateway {
    PagamentoResponseDTO solicitarPagamento(PagamentoRequestDTO request);
}