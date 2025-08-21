package br.com.fiap.mspedidoreciver.adapter.controller.request;

import java.util.List;

public record PedidoReciverResponseDTO(
    Long clienteId,
    List<ItemPedidoResponseDTO> itens,
    String numeroCartao,
    String status
) {
}
