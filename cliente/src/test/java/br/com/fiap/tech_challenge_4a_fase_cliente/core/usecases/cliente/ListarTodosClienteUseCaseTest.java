package br.com.fiap.tech_challenge_4a_fase_cliente.core.usecases.cliente;

import br.com.fiap.tech_challenge_4a_fase_cliente.TechChallenge4aFaseClienteApplication;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.domain.entities.cliente.Cliente;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.gateways.ClienteGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = TechChallenge4aFaseClienteApplication.class)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes Unitários do Caso de Uso de Listagem de Clientes")
class ListarTodosClienteUseCaseTest {

    private ClienteGateway clienteGateway;
    private ListarTodosClienteUseCase listarTodosClienteUseCase;

    @BeforeEach
    void setUp() {
        clienteGateway = mock(ClienteGateway.class);
        listarTodosClienteUseCase = new ListarTodosClienteUseCase(clienteGateway);
    }

    @Test
    @DisplayName("Deve retornar lista com todos os clientes quando existirem registros")
    void deveRetornarListaDeClientesQuandoExistiremClientes() {
        Cliente cliente1 = new Cliente(1L, "João", "123.456.789-10", LocalDate.of(1990, 1, 1), null);
        Cliente cliente2 = new Cliente(2L, "Maria", "987.654.321-00", LocalDate.of(1992, 2, 2), null);
        List<Cliente> clientes = Arrays.asList(cliente1, cliente2);

        when(clienteGateway.listarTodos()).thenReturn(clientes);

        List<Cliente> resultado = listarTodosClienteUseCase.executar();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(cliente1));
        assertTrue(resultado.contains(cliente2));
        verify(clienteGateway).listarTodos();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não existirem clientes cadastrados")
    void deveRetornarListaVaziaQuandoNaoExistiremClientes() {
        when(clienteGateway.listarTodos()).thenReturn(Collections.emptyList());

        List<Cliente> resultado = listarTodosClienteUseCase.executar();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(clienteGateway).listarTodos();
    }
}