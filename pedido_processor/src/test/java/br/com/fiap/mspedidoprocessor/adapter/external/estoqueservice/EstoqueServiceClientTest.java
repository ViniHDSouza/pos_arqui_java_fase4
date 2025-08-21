package br.com.fiap.mspedidoprocessor.adapter.external.estoqueservice;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.fiap.mspedidoprocessor.adapter.external.estoqueservice.dto.BaixaEstoqueRequestDTO;
import br.com.fiap.mspedidoprocessor.adapter.external.estoqueservice.dto.EstoqueResponseDTO;
import br.com.fiap.mspedidoprocessor.core.gateways.EstoqueServiceGateway;

class EstoqueServiceClientTest {

    @Mock
    private EstoqueServiceGateway estoqueServiceGateway;

    @InjectMocks
    private EstoqueServiceClientTestWrapper estoqueClientWrapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveConsultarProdutoComSucesso() {
        String sku = "ABC123";
        EstoqueResponseDTO esperado = new EstoqueResponseDTO(sku, 10);
        when(estoqueServiceGateway.consultaProduto(sku)).thenReturn(esperado);

        EstoqueResponseDTO resultado = estoqueClientWrapper.consultarProduto(sku);

        assertNotNull(resultado);
    }

    @Test
    void deveChamarBaixaEstoque() {
        BaixaEstoqueRequestDTO request = new BaixaEstoqueRequestDTO("ABC123", 2);
        doNothing().when(estoqueServiceGateway).baixaEstoque(request);

        assertDoesNotThrow(() -> estoqueClientWrapper.baixarEstoque(request));
        verify(estoqueServiceGateway, times(1)).baixaEstoque(request);
    }

    @Test
    void deveChamarReverterEstoque() {
        BaixaEstoqueRequestDTO request = new BaixaEstoqueRequestDTO("XYZ789", 5);
        doNothing().when(estoqueServiceGateway).reverterEstoque(request);

        assertDoesNotThrow(() -> estoqueClientWrapper.reverterEstoque(request));
        verify(estoqueServiceGateway, times(1)).reverterEstoque(request);
    }

    // Wrapper para simular o uso do FeignClient
    static class EstoqueServiceClientTestWrapper {
        private final EstoqueServiceGateway estoqueServiceGateway;

        public EstoqueServiceClientTestWrapper(EstoqueServiceGateway estoqueServiceGateway) {
            this.estoqueServiceGateway = estoqueServiceGateway;
        }

        public EstoqueResponseDTO consultarProduto(String sku) {
            return estoqueServiceGateway.consultaProduto(sku);
        }

        public void baixarEstoque(BaixaEstoqueRequestDTO request) {
            estoqueServiceGateway.baixaEstoque(request);
        }

        public void reverterEstoque(BaixaEstoqueRequestDTO request) {
            estoqueServiceGateway.reverterEstoque(request);
        }
    }
}
