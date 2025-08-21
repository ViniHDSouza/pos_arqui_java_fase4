package br.com.fiap.msestoque.core.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

class EstornarEstoqueUseCaseTest {

    private EstoqueGateway estoqueGateway;
    private EstornarEstoqueUseCase useCase;

    @BeforeEach
    void setUp() {
        estoqueGateway = mock(EstoqueGateway.class);
        useCase = new EstornarEstoqueUseCase(estoqueGateway);
    }

    @Test
    @DisplayName("Deve lançar exceção se quantidade for nula")
    void deveLancarExcecaoSeQuantidadeForNula() {
        EstoqueException ex = assertThrows(EstoqueException.class, () -> useCase.executar("SKU123", null));
        assertEquals("Quantidade não pode ser nulo.", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção se SKU for nulo ou vazio")
    void deveLancarExcecaoSeSkuForInvalido() {
        assertThrows(EstoqueException.class, () -> useCase.executar(null, 10));
        assertThrows(EstoqueException.class, () -> useCase.executar("", 10));
    }

    @Test
    @DisplayName("Deve lançar exceção se o estoque não for encontrado")
    void deveLancarExcecaoSeEstoqueNaoEncontrado() {
        when(estoqueGateway.findBySku("ABC123")).thenReturn(Optional.empty());

        EstoqueException ex = assertThrows(EstoqueException.class, () -> useCase.executar("ABC123", 5));
        assertTrue(ex.getMessage().contains("Estoque não encontrado"));
    }

    @Test
    @DisplayName("Deve estornar a quantidade corretamente no estoque existente")
    void deveEstornarQuantidadeComSucesso() {
        Estoque estoque = new Estoque();
        estoque.setSku("SKU001");
        estoque.setQuantidade(10);

        when(estoqueGateway.findBySku("SKU001")).thenReturn(Optional.of(estoque));

        useCase.executar("SKU001", 5);

        assertEquals(15, estoque.getQuantidade());
        assertNotNull(estoque.getAtualizadoEm());
        verify(estoqueGateway).save(estoque);
    }
}