package br.com.fiap.mspedidoprocessor.adapter.mapper;

import br.com.fiap.mspedidoprocessor.adapter.controller.response.PagamentoCallbackResponseDTO;
import br.com.fiap.mspedidoprocessor.adapter.external.clientkafka.dto.Pedido;
import br.com.fiap.mspedidoprocessor.adapter.external.pagamentoservice.dto.PagamentoRequestDTO;
import br.com.fiap.mspedidoprocessor.adapter.persistence.entity.ItemPedidoProcessorEntity;
import br.com.fiap.mspedidoprocessor.adapter.persistence.entity.PedidoProcessorEntity;
import br.com.fiap.mspedidoprocessor.core.domain.ItemPedidoProcessor;
import br.com.fiap.mspedidoprocessor.core.domain.PagamentoCallback;
import br.com.fiap.mspedidoprocessor.core.domain.PedidoProcessor;
import br.com.fiap.mspedidoprocessor.core.domain.PedidoStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PedidoProcessorMapper {

    public PedidoProcessor toModel(PedidoProcessorEntity entity) {
        if (entity == null) {
            return null;
        }

        return PedidoProcessor.builder()
                .id(UUID.fromString(entity.getId().toString()))
                .pedidoReciverId(entity.getPedidoReciverId())
                .clienteId(entity.getClienteId())
                .total(entity.getTotal())
                .status(PedidoStatus.valueOf(entity.getStatus()))
                .itens(entity.getItens().stream()
                        .map(this::toItemPedidoProcessor)
                        .toList())
                .build();
    }

    private ItemPedidoProcessor toItemPedidoProcessor(ItemPedidoProcessorEntity entity) {
        if (entity == null) {
            return null;
        }
        return ItemPedidoProcessor.builder()
                .sku(entity.getSku())
                .quantidade(entity.getQuantidade())
                .precoUnitario(entity.getPrecoUnitario())
                .precoTotal(entity.getPrecoTotal())
                .build();
    }

    public PedidoProcessorEntity toEntity(PedidoProcessor model) {
        if (model == null) {
            return null;
        }


        return PedidoProcessorEntity.builder()
                .id(model.getId().toString())
                .pedidoReciverId(model.getPedidoReciverId())
                .clienteId(model.getClienteId())
                .total(model.getTotal())
                .status(model.getStatus().name())
                .criadoEm(LocalDateTime.now())
                .itens(model.getItens().stream()
                        .map(this::toItemPedidoProcessorEntity)
                        .toList())
                .build();
    }

    private ItemPedidoProcessorEntity toItemPedidoProcessorEntity(ItemPedidoProcessor item) {
        if (item == null) {
            return null;
        }
        return ItemPedidoProcessorEntity.builder()
                .sku(item.getSku())
                .quantidade(item.getQuantidade())
                .precoUnitario(item.getPrecoUnitario())
                .precoTotal(item.getPrecoTotal())
                .build();
    }

    public PagamentoRequestDTO toPagamentoRequestDTO(PedidoProcessor pedidoProcessor) {
        if (pedidoProcessor == null) {
            return null;
        }

        return new PagamentoRequestDTO(
                pedidoProcessor.getId().toString(),
                pedidoProcessor.getTotal(),
                pedidoProcessor.getNumeroCartao()
        );
    }

    private UUID longToUUID(String value) {
        return value != null ? UUID.fromString(value.toString()) : null;
    }

    public PedidoProcessor toPedidoProcessor(Pedido pedido) {
        if (pedido == null) {
            return null;
        }

        return PedidoProcessor.builder()
                .pedidoReciverId(pedido.getId())
                .status(PedidoStatus.valueOf(pedido.getStatus().name()))
                .clienteId(pedido.getClienteId())
                .numeroCartao(pedido.getNumeroCartao())
                .itens(pedido.getItens().stream()
                        .map(item -> ItemPedidoProcessor.builder()
                                .sku(item.getSku())
                                .quantidade(item.getQuantidade())
                                .precoUnitario(BigDecimal.ZERO)
                                .precoTotal(BigDecimal.ZERO)
                                .build())
                        .collect(Collectors.toList()))
                .total(BigDecimal.ZERO)
                .build();
    }


    public PagamentoCallbackResponseDTO toPagamentoCallbackResponseDTO(PagamentoCallback callback) {
        if (callback==null){
            return null;
        }
        return new PagamentoCallbackResponseDTO(
                callback.getPedidoId(),
                callback.getClienteId(),
                callback.getStatus()
        );
    }

    public PagamentoCallback toPagamentoCallback(PedidoProcessorEntity entity) {
        if (entity == null) {
            return null;
        }
        return new PagamentoCallback(
            entity.getPedidoReciverId(),
                entity.getClienteId(),
            entity.getStatus().toString()
        );
    }
}