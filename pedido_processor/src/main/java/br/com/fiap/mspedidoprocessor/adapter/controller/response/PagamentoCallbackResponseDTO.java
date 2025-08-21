package br.com.fiap.mspedidoprocessor.adapter.controller.response;

public record PagamentoCallbackResponseDTO(
        Long pedidoId,
        Long clienteId,
        String status
) {
}

