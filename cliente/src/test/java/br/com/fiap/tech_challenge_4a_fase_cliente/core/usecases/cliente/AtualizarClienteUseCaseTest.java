package br.com.fiap.tech_challenge_4a_fase_cliente.core.usecases.cliente;

import br.com.fiap.tech_challenge_4a_fase_cliente.TechChallenge4aFaseClienteApplication;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.domain.entities.cliente.Cliente;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.exception.ClienteNaoEncontradoException;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.gateways.ClienteGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = TechChallenge4aFaseClienteApplication.class)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes Unitários do Caso de Uso de Atualização de Cliente")
class AtualizarClienteUseCaseTest {

    private ClienteGateway clienteGateway;
    private AtualizarClienteUseCase useCase;

    @BeforeEach
    void setUp() {
        clienteGateway = mock(ClienteGateway.class);
        useCase = new AtualizarClienteUseCase(clienteGateway);
    }

    @Test
    @DisplayName("Deve atualizar cliente quando ele existir na base")
    void deveAtualizarClienteQuandoExistente() {
        Long id = 1L;
        Cliente cliente = new Cliente(id, "Nome", "123.456.789-10", LocalDate.of(1990, 1, 1), null);

        when(clienteGateway.buscarPorId(id)).thenReturn(Optional.of(cliente));
        when(clienteGateway.atualizar(id, cliente)).thenReturn(cliente);

        Cliente atualizado = useCase.executar(id, cliente);

        assertNotNull(atualizado);
        assertEquals(cliente, atualizado);
        verify(clienteGateway).buscarPorId(id);
        verify(clienteGateway).atualizar(id, cliente);
    }

    @Test
    @DisplayName("Deve lançar exceção quando tentar atualizar cliente inexistente")
    void deveLancarExcecaoQuandoClienteNaoEncontrado() {
        Long id = 2L;
        Cliente cliente = new Cliente(id, "Nome", "123.456.789-10", LocalDate.of(1990, 1, 1), null);

        when(clienteGateway.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(ClienteNaoEncontradoException.class, () -> useCase.executar(id, cliente));
        verify(clienteGateway).buscarPorId(id);
        verify(clienteGateway, never()).atualizar(anyLong(), any());
    }
}