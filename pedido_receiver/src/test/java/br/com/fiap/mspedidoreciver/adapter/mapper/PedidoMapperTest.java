package br.com.fiap.mspedidoreciver.adapter.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.mspedidoreciver.adapter.controller.request.ItemPedidoResponseDTO;
import br.com.fiap.mspedidoreciver.adapter.controller.request.PedidoReciverResponseDTO;
import br.com.fiap.mspedidoreciver.adapter.controller.response.PedidoReciverRequestDTO;
import br.com.fiap.mspedidoreciver.adapter.persistence.entity.ItemPedidoEntity;
import br.com.fiap.mspedidoreciver.adapter.persistence.entity.PedidoEntity;
import br.com.fiap.mspedidoreciver.core.domain.ItemPedido;
import br.com.fiap.mspedidoreciver.core.domain.Pedido;

class PedidoMapperTest {

    private PedidoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PedidoMapper();
    }

    @Test
    void deveConverterPedidoEntityParaDomain() {
        ItemPedidoEntity item = ItemPedidoEntity.builder()
                .id(1L).sku("SKU001").quantidade(2).build();

        PedidoEntity entity = PedidoEntity.builder()
                .id(100L)
                .clienteId(123L)
                .numeroCartao("1111-2222-3333-4444")
                .itens(List.of(item))
                .status("CRIADO")
                .dataCriacao(LocalDateTime.now())
                .build();

        Pedido domain = mapper.toPedidoDomain(entity);

        assertNotNull(domain);
        assertEquals(entity.getClienteId(), domain.getClienteId());
        assertEquals("SKU001", domain.getItens().get(0).getSku());
    }

    @Test
    void deveConverterPedidoDomainParaEntity() {
        ItemPedido item = ItemPedido.builder()
                .id(1L).sku("SKU002").quantidade(5).build();

        Pedido domain = Pedido.builder()
                .id(100L)
                .clienteId(456L)
                .numeroCartao("5555-6666-7777-8888")
                .itens(List.of(item))
                .status("PAGO")
                .dataCriacao(LocalDateTime.now())
                .build();

        PedidoEntity entity = mapper.toPedidoEntity(domain);

        assertNotNull(entity);
        assertEquals("SKU002", entity.getItens().get(0).getSku());
        assertEquals(domain.getClienteId(), entity.getClienteId());
    }

    @Test
    void deveConverterDtoParaPedidoDomain() {
        ItemPedidoResponseDTO itemDto = new ItemPedidoResponseDTO("SKU003", 3);
        PedidoReciverResponseDTO dto = new PedidoReciverResponseDTO(
                789L,
                List.of(itemDto),
                "9999-0000-1111-2222",
                "CRIADO"
        );

        Pedido pedido = mapper.toPedidoDomain(dto);

        assertNotNull(pedido);
        assertEquals("SKU003", pedido.getItens().get(0).getSku());
    }

    @Test
    void deveConverterPedidoParaDto() {
        ItemPedido item = ItemPedido.builder().sku("SKUX").quantidade(1).build();
        Pedido pedido = Pedido.builder()
                .clienteId(10L)
                .numeroCartao("1234")
                .itens(List.of(item))
                .status("FINALIZADO")
                .build();

        PedidoReciverResponseDTO dto = mapper.toPedidoReciverResponseDTO(pedido);

        assertNotNull(dto);
        assertEquals("SKUX", dto.itens().get(0).sku());
        assertEquals("FINALIZADO", dto.status());
    }

    @Test
    void deveConverterParaPedidoReciverRequestDTO() {
        Pedido pedido = Pedido.builder()
                .id(100L)
                .clienteId(1L)
                .numeroCartao("4321")
                .status("PAGO")
                .dataCriacao(LocalDateTime.now())
                .build();

        PedidoReciverRequestDTO result = mapper.toPedidoReciverRequestDTO(pedido);

        assertNotNull(result);
        assertEquals("4321", result.getNumeroCartao());
        assertEquals("PAGO", result.getStatus());
    }
}
