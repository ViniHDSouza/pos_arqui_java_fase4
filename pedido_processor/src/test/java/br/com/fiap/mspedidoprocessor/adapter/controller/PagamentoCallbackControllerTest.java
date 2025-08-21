package br.com.fiap.mspedidoprocessor.adapter.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.fiap.mspedidoprocessor.adapter.controller.response.PagamentoCallbackResponseDTO;
import br.com.fiap.mspedidoprocessor.adapter.mapper.PedidoProcessorMapper;
import br.com.fiap.mspedidoprocessor.core.domain.PagamentoCallback;
import br.com.fiap.mspedidoprocessor.core.usecase.pedidoprocessor.ConsultaPedidoProcessorUseCase;

class PagamentoCallbackControllerTest {

    @Mock
    private ConsultaPedidoProcessorUseCase consultaPedidoProcessorUseCase;

    @Mock
    private PedidoProcessorMapper pedidoProcessorMapper;

    @InjectMocks
    private PagamentoCallbackController controller;

    public PagamentoCallbackControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarPagamentoQuandoExistir() {
        Long id = 1L;
        PagamentoCallback pagamento = new PagamentoCallback(id, id, null); // Adapte se necessário
        PagamentoCallbackResponseDTO dto = new PagamentoCallbackResponseDTO(id, id, null);

        when(consultaPedidoProcessorUseCase.consultarPagamentoCallback(id)).thenReturn(pagamento);
        when(pedidoProcessorMapper.toPagamentoCallbackResponseDTO(pagamento)).thenReturn(dto);

        ResponseEntity<PagamentoCallbackResponseDTO> response = controller.consultaPagamento(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void deveRetornar404QuandoNaoExistir() {
        Long id = 2L;
        PagamentoCallback pagamento = new PagamentoCallback(id, id, null); // Adapte se necessário

        when(consultaPedidoProcessorUseCase.consultarPagamentoCallback(id)).thenReturn(pagamento);
        when(pedidoProcessorMapper.toPagamentoCallbackResponseDTO(pagamento)).thenReturn(null);

        ResponseEntity<PagamentoCallbackResponseDTO> response = controller.consultaPagamento(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
