package br.com.fiap.mspedidoprocessor.core.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class EstoqueInsuficienteExceptionTest {

    @Test
    void deveCriarExceptionComSkuQuantidadeSolicitadaEDisponivel() {
        String sku = "ABC123";
        int solicitada = 10;
        int disponivel = 5;

        EstoqueInsuficienteException exception =
                new EstoqueInsuficienteException(sku, solicitada, disponivel);

        assertNotNull(exception);
        assertEquals("Estoque insuficiente para o produto ABC123. Solicitado: 10, Disponível: 5", exception.getMessage());
    }

    @Test
    void deveCriarExceptionComApenasSku() {
        String sku = "XYZ789";

        EstoqueInsuficienteException exception = new EstoqueInsuficienteException(sku);

        assertNotNull(exception);
        assertEquals("Estoque insuficiente para o produto XYZ789.", exception.getMessage());
    }
}
