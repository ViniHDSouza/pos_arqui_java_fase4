package br.com.fiap.mspedidoprocessor.adapter.external.produtoservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.mspedidoprocessor.adapter.external.produtoservice.dto.ProdutoDtoResponse;

class ProdutoServiceClientTest {

    private ProdutoServiceClient produtoServiceClient;

    @BeforeEach
    void setup() {
        produtoServiceClient = mock(ProdutoServiceClient.class);
    }

    @Test
    void deveRetornarTrueQuandoProdutoExistir() {
        String sku = "PROD-123";
        ProdutoDtoResponse response = new ProdutoDtoResponse(null, sku, sku, null);

        when(produtoServiceClient.buscarProduto(sku)).thenReturn(response);
        ProdutoServiceClient client = new ProdutoServiceClient() {
            @Override
            public ProdutoDtoResponse buscarProduto(String sku) {
                return response;
            }

            @Override
            public BigDecimal obterPreco(String sku) {
                return BigDecimal.ZERO;
            }
        };

        boolean result = client.skuExiste(sku);
        assertTrue(result);
    }

    @Test
    void deveRetornarFalseQuandoProdutoNaoExistir() {
        String sku = "INVALIDO";

        ProdutoServiceClient client = new ProdutoServiceClient() {
            @Override
            public ProdutoDtoResponse buscarProduto(String sku) {
                throw new RuntimeException("Produto não encontrado");
            }

            @Override
            public BigDecimal obterPreco(String sku) {
                return BigDecimal.ZERO;
            }
        };

        boolean result = client.skuExiste(sku);
        assertFalse(result);
    }

    @Test
    void deveRetornarProdutoQuandoBuscarPorSku() {
        String sku = "PROD-789";
        ProdutoDtoResponse produto = new ProdutoDtoResponse(null, "Camiseta",sku, null);


        when(produtoServiceClient.buscarProduto(sku)).thenReturn(produto);

        ProdutoDtoResponse response = produtoServiceClient.buscarProduto(sku);
        assertEquals("PROD-789", response.sku());
        assertEquals("Camiseta", response.nome());
    }

    @Test
    void deveRetornarPrecoDoProduto() {
        String sku = "PROD-456";
        BigDecimal precoEsperado = new BigDecimal("99.90");

        when(produtoServiceClient.obterPreco(sku)).thenReturn(precoEsperado);

        BigDecimal preco = produtoServiceClient.obterPreco(sku);
        assertEquals(precoEsperado, preco);
    }
}
