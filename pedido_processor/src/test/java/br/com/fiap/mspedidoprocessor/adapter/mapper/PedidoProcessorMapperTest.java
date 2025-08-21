package br.com.fiap.mspedidoprocessor.adapter.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.mspedidoprocessor.adapter.controller.response.PagamentoCallbackResponseDTO;
import br.com.fiap.mspedidoprocessor.adapter.external.clientkafka.dto.ItemPedido;
import br.com.fiap.mspedidoprocessor.adapter.external.clientkafka.dto.Pedido;
import br.com.fiap.mspedidoprocessor.adapter.external.clientkafka.dto.PedidoStatus;
import br.com.fiap.mspedidoprocessor.adapter.persistence.entity.ItemPedidoProcessorEntity;
import br.com.fiap.mspedidoprocessor.adapter.persistence.entity.PedidoProcessorEntity;
import br.com.fiap.mspedidoprocessor.core.domain.ItemPedidoProcessor;
import br.com.fiap.mspedidoprocessor.core.domain.PagamentoCallback;
import br.com.fiap.mspedidoprocessor.core.domain.PedidoProcessor;

class PedidoProcessorMapperTest {

    private PedidoProcessorMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PedidoProcessorMapper();
    }

    @Test
    void toModel_deveConverterEntityParaModel() {
        PedidoProcessorEntity entity = PedidoProcessorEntity.builder()
                .id(UUID.randomUUID().toString())
                .pedidoReciverId(1L)
                .clienteId(2L)
                .total(BigDecimal.TEN)
                .status("ABERTO")
                .itens(List.of(ItemPedidoProcessorEntity.builder()
                        .sku("SKU001")
                        .quantidade(1)
                        .precoUnitario(BigDecimal.ONE)
                        .precoTotal(BigDecimal.ONE)
                        .build()))
                .build();

        PedidoProcessor model = mapper.toModel(entity);

        assertNotNull(model);
        assertEquals(entity.getPedidoReciverId(), model.getPedidoReciverId());
        assertEquals(1, model.getItens().size());
        assertEquals("SKU001", model.getItens().get(0).getSku());
    }

    @Test
    void toEntity_deveConverterModelParaEntity() {
        PedidoProcessor model = PedidoProcessor.builder()
                .id(UUID.randomUUID())
                .pedidoReciverId(1L)
                .clienteId(2L)
                .status(br.com.fiap.mspedidoprocessor.core.domain.PedidoStatus.ABERTO)
                .total(BigDecimal.TEN)
                .itens(List.of(ItemPedidoProcessor.builder()
                        .sku("SKU001")
                        .quantidade(1)
                        .precoUnitario(BigDecimal.ONE)
                        .precoTotal(BigDecimal.ONE)
                        .build()))
                .build();

        PedidoProcessorEntity entity = mapper.toEntity(model);

        assertNotNull(entity);
        assertEquals(model.getPedidoReciverId(), entity.getPedidoReciverId());
        assertEquals(1, entity.getItens().size());
        assertEquals("SKU001", entity.getItens().get(0).getSku());
    }

    @Test
    void toPagamentoCallbackResponseDTO_deveMapearCorretamente() {
        PagamentoCallback callback = new PagamentoCallback(1L, 2L, "PAGO");

        PagamentoCallbackResponseDTO dto = mapper.toPagamentoCallbackResponseDTO(callback);

        assertNotNull(dto);
        assertEquals(1L, dto.pedidoId());
        assertEquals(2L, dto.clienteId());
        assertEquals("PAGO", dto.status());
    }

    @Test
    void toPagamentoCallback_deveMapearCorretamente() {
        PedidoProcessorEntity entity = PedidoProcessorEntity.builder()
                .pedidoReciverId(1L)
                .clienteId(2L)
                .status("PAGO")
                .build();

        PagamentoCallback callback = mapper.toPagamentoCallback(entity);

        assertNotNull(callback);
        assertEquals(1L, callback.getPedidoId());
        assertEquals(2L, callback.getClienteId());
        assertEquals("PAGO", callback.getStatus());
    }

    @Test
    void toPedidoProcessor_deveConverterPedidoKafka() {
        Pedido pedido = new Pedido();
        pedido.setId(99L);
        pedido.setClienteId(7L);
        pedido.setNumeroCartao("123");
        pedido.setStatus(PedidoStatus.ABERTO);
        ItemPedido item = new ItemPedido();
        item.setSku("SKU001");
        item.setQuantidade(2);
        pedido.setItens(List.of(item));

        PedidoProcessor result = mapper.toPedidoProcessor(pedido);

        assertNotNull(result);
        assertEquals(99L, result.getPedidoReciverId());
        assertEquals(7L, result.getClienteId());
        assertEquals("123", result.getNumeroCartao());
        assertEquals(1, result.getItens().size());
        assertEquals("SKU001", result.getItens().get(0).getSku());
    }
}
