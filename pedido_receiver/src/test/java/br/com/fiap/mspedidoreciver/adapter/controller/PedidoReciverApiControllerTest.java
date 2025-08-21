package br.com.fiap.mspedidoreciver.adapter.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.fiap.mspedidoreciver.adapter.controller.request.ItemPedidoResponseDTO;
import br.com.fiap.mspedidoreciver.adapter.controller.request.PedidoReciverResponseDTO;
import br.com.fiap.mspedidoreciver.adapter.controller.response.PedidoReciverRequestDTO;
import br.com.fiap.mspedidoreciver.adapter.mapper.PedidoMapper;
import br.com.fiap.mspedidoreciver.core.domain.Pedido;
import br.com.fiap.mspedidoreciver.core.usecase.pedidoreciver.CriarPedidoReciverUseCase;

class PedidoReciverApiControllerTest {

    @Mock
    private CriarPedidoReciverUseCase criarPedidoReciverUseCase;

    @Mock
    private PedidoMapper pedidoMapper;

    @InjectMocks
    private PedidoReciverApiController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarPedidoComSucesso() {
        // Arrange
        ItemPedidoResponseDTO item1 = new ItemPedidoResponseDTO("SKU123", 2);
        ItemPedidoResponseDTO item2 = new ItemPedidoResponseDTO("SKU456", 1);

        PedidoReciverResponseDTO pedidoDTO = new PedidoReciverResponseDTO(
                1L,
                List.of(item1, item2),
                "1234-5678-9012-3456",
                "ABERTO"
        );
        
        PedidoReciverResponseDTO requestDTO = pedidoDTO;
        Pedido pedidoInput = new Pedido();
        Pedido pedidoRetorno = new Pedido();
        PedidoReciverRequestDTO responseDTO = new PedidoReciverRequestDTO();

        when(pedidoMapper.toPedidoDomain(requestDTO)).thenReturn(pedidoInput);
        when(criarPedidoReciverUseCase.execute(pedidoInput)).thenReturn(pedidoRetorno);
        when(pedidoMapper.toPedidoReciverRequestDTO(pedidoRetorno)).thenReturn(responseDTO);

        // Act
        ResponseEntity<PedidoReciverRequestDTO> response = controller.criarPedido(requestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }
}
