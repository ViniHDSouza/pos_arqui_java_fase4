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

class BaixaEstoqueUseCaseTest {

    private EstoqueGateway estoqueGateway;
    private BaixaEstoqueUseCase useCase;

    @BeforeEach
    void setUp() {
        estoqueGateway = mock(EstoqueGateway.class);
        useCase = new BaixaEstoqueUseCase(estoqueGateway);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a quantidade for nula ou zero")
    void deveLancarExcecaoQuandoQuantidadeInvalida() {
        assertThrows(EstoqueException.class, () -> useCase.executar("SKU001", null));
        assertThrows(EstoqueException.class, () -> useCase.executar("SKU001", 0));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o SKU for nulo ou vazio")
    void deveLancarExcecaoQuandoSkuInvalido() {
        assertThrows(EstoqueException.class, () -> useCase.executar(null, 1));
        assertThrows(EstoqueException.class, () -> useCase.executar("", 1));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o SKU não for encontrado")
    void deveLancarExcecaoQuandoEstoqueNaoEncontrado() {
        when(estoqueGateway.findBySku("SKU404")).thenReturn(Optional.empty());

        EstoqueException ex = assertThrows(EstoqueException.class, () -> useCase.executar("SKU404", 5));
        assertTrue(ex.getMessage().contains("Estoque não encontrado"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando não houver estoque suficiente")
    void deveLancarExcecaoQuandoEstoqueInsuficiente() {
        Estoque estoque = new Estoque();
        estoque.setSku("SKU001");
        estoque.setQuantidade(2);

        when(estoqueGateway.findBySku("SKU001")).thenReturn(Optional.of(estoque));

        EstoqueException ex = assertThrows(EstoqueException.class, () -> useCase.executar("SKU001", 5));
        assertTrue(ex.getMessage().contains("Estoque insuficiente"));
    }

    @Test
    @DisplayName("Deve realizar baixa de estoque com sucesso")
    void deveRealizarBaixaComSucesso() {
        Estoque estoque = new Estoque();
        estoque.setSku("SKU001");
        estoque.setQuantidade(10);

        when(estoqueGateway.findBySku("SKU001")).thenReturn(Optional.of(estoque));

        useCase.executar("SKU001", 4);

        assertEquals(6, estoque.getQuantidade());
        assertNotNull(estoque.getAtualizadoEm());
        verify(estoqueGateway).save(estoque);
    }
}
