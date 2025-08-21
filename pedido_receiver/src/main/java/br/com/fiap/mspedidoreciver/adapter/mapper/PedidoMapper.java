package br.com.fiap.mspedidoreciver.adapter.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.fiap.mspedidoreciver.adapter.controller.request.ItemPedidoResponseDTO;
import br.com.fiap.mspedidoreciver.adapter.controller.request.PedidoReciverResponseDTO;
import br.com.fiap.mspedidoreciver.adapter.controller.response.PedidoReciverRequestDTO;
import br.com.fiap.mspedidoreciver.adapter.persistence.entity.ItemPedidoEntity;
import br.com.fiap.mspedidoreciver.adapter.persistence.entity.PedidoEntity;
import br.com.fiap.mspedidoreciver.core.domain.ItemPedido;
import br.com.fiap.mspedidoreciver.core.domain.Pedido;

@Component
public class PedidoMapper {

    public Pedido toPedidoDomain(PedidoEntity entity) {
        if (entity == null) return null;

        return Pedido.builder()
                .id(entity.getId())
                .clienteId(entity.getClienteId())
                .numeroCartao(entity.getNumeroCartao())
                .itens(entity.getItens().stream()
                        .map(this::itemToDomain)
                        .collect(Collectors.toList()))
                .status(entity.getStatus())
                .dataCriacao(entity.getDataCriacao())
                .build();
    }

    public PedidoEntity toPedidoEntity(Pedido domain) {
        if (domain == null) return null;

        return PedidoEntity.builder()
                .id(domain.getId())
                .clienteId(domain.getClienteId())
                .numeroCartao(domain.getNumeroCartao())
                .itens(domain.getItens().stream()
                        .map(this::itemToEntity)
                        .collect(Collectors.toList()))
                .status(domain.getStatus())
                .dataCriacao(domain.getDataCriacao())
                .build();
    }

    public Pedido toPedidoDomain(PedidoReciverResponseDTO dto) {
        if (dto == null) return null;
        return Pedido.builder()
                .clienteId(dto.clienteId())
                .numeroCartao(dto.numeroCartao())
                .itens(dto.itens() != null ? dto.itens().stream()
                        .map(this::itemPedidoResponseDTOToDomain)
                        .collect(Collectors.toList()) : List.of())
                .status(dto.status())
                .build();
    }

    public PedidoReciverResponseDTO toPedidoReciverResponseDTO(Pedido pedido) {
        if (pedido == null) return null;
        return new PedidoReciverResponseDTO(
                pedido.getClienteId(),
                pedido.getItens() != null ? pedido.getItens().stream()
                        .map(this::itemPedidoToResponseDTO)
                        .collect(Collectors.toList()) : List.of(),
                pedido.getNumeroCartao(),
                pedido.getStatus()
        );
    }

    private ItemPedido itemToDomain(ItemPedidoEntity entity) {
        return ItemPedido.builder()
                .id(entity.getId())
                .sku(entity.getSku())
                .quantidade(entity.getQuantidade())
                .build();
    }

    private ItemPedidoEntity itemToEntity(ItemPedido domain) {
        return ItemPedidoEntity.builder()
                .id(domain.getId())
                .sku(domain.getSku())
                .quantidade(domain.getQuantidade())
                .build();
    }

    private ItemPedido itemPedidoResponseDTOToDomain(ItemPedidoResponseDTO dto) {
        if (dto == null) return null;
        return ItemPedido.builder()
                .sku(dto.sku())
                .quantidade(dto.quantidade())
                .build();
    }

    private ItemPedidoResponseDTO itemPedidoToResponseDTO(ItemPedido item) {
        if (item == null) return null;
        return new ItemPedidoResponseDTO(
                item.getSku(),
                item.getQuantidade()
        );
    }

    public PedidoReciverRequestDTO toPedidoReciverRequestDTO(Pedido pedidoRetorno) {
        return new PedidoReciverRequestDTO()
                .builder()
                .clienteId(pedidoRetorno.getClienteId())
                .id(pedidoRetorno.getId())
                .dataCriacao(pedidoRetorno.getDataCriacao())
                .status(pedidoRetorno.getStatus())
                .numeroCartao(pedidoRetorno.getNumeroCartao())
                .build();
    }
}