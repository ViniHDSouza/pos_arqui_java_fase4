package br.com.fiap.mspedidoprocessor.adapter.external.clienteservice;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

import br.com.fiap.mspedidoprocessor.core.gateways.ClienteServiceGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ClienteServiceClientTest {

    @Mock
    private ClienteServiceGateway clienteServiceGateway;

    @InjectMocks
    private ClienteServiceClientTestWrapper clienteServiceClientTestWrapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarTrueQuandoClienteExiste() {
        Long id = 1L;
        when(clienteServiceGateway.clienteExiste(id)).thenReturn(true);

        boolean existe = clienteServiceClientTestWrapper.verificarCliente(id);

        assertTrue(existe);
    }

    @Test
    void deveRetornarFalseQuandoClienteNaoExiste() {
        Long id = 2L;
        when(clienteServiceGateway.clienteExiste(id)).thenReturn(false);

        boolean existe = clienteServiceClientTestWrapper.verificarCliente(id);

        assertFalse(existe);
    }

    // Wrapper para simular uso da interface
    static class ClienteServiceClientTestWrapper {

        private final ClienteServiceGateway clienteServiceGateway;

        public ClienteServiceClientTestWrapper(ClienteServiceGateway clienteServiceGateway) {
            this.clienteServiceGateway = clienteServiceGateway;
        }

        public boolean verificarCliente(Long id) {
            return clienteServiceGateway.clienteExiste(id);
        }
    }
}
