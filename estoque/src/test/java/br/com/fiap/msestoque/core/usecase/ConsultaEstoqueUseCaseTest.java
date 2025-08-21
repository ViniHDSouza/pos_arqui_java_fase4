package br.com.fiap.msestoque.core.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fiap.msestoque.core.domain.Estoque;
import br.com.fiap.msestoque.core.exception.EstoqueException;
import br.com.fiap.msestoque.core.gateways.EstoqueGateway;

class ConsultaEstoqueUseCaseTest {

    private EstoqueGateway estoqueGateway;
    private ConsultaEstoqueUseCase useCase;

    @BeforeEach
    void setUp() {
        estoqueGateway = mock(EstoqueGateway.class);
        useCase = new ConsultaEstoqueUseCase(estoqueGateway);
    }

    @Test
    @DisplayName("Deve lançar exceção se SKU for nulo ou vazio")
    void deveLancarExcecaoSeSkuForInvalido() {
        assertThrows(EstoqueException.class, () -> useCase.executar(null));
        assertThrows(EstoqueException.class, () -> useCase.executar(""));
    }

    @Test
    @DisplayName("Deve lançar exceção se o SKU não for encontrado")
    void deveLancarExcecaoSeEstoqueNaoEncontrado() {
        when(estoqueGateway.findBySku("ABC123")).thenReturn(Optional.empty());

        EstoqueException ex = assertThrows(EstoqueException.class, () -> useCase.executar("ABC123"));
        assertTrue(ex.getMessage().contains("Estoque não encontrado"));
    }

    @Test
    @DisplayName("Deve retornar quantidade de estoque corretamente")
    void deveRetornarQuantidadeCorretamente() {
        Estoque estoque = new Estoque();
        estoque.setSku("SKU001");
        estoque.setQuantidade(42);

        when(estoqueGateway.findBySku("SKU001")).thenReturn(Optional.of(estoque));

        int quantidade = useCase.executar("SKU001");

        assertEquals(42, quantidade);
        verify(estoqueGateway).findBySku("SKU001");
    }
}