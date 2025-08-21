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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TechChallenge4aFaseClienteApplication.class)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes Unitários do Caso de Uso de Busca de Cliente")
class BuscarClienteUseCaseTest {

    private ClienteGateway clienteGateway;
    private BuscarClienteUseCase buscarClienteUseCase;

    @BeforeEach
    void setUp() {
        clienteGateway = mock(ClienteGateway.class);
        buscarClienteUseCase = new BuscarClienteUseCase(clienteGateway);
    }

    @Test
    @DisplayName("Deve retornar cliente quando ele existir na base")
    void deveRetornarClienteQuandoExistente() {
        Long id = 1L;
        Cliente cliente = new Cliente(id, "João", "123.456.789-10", LocalDate.of(1990, 1, 1), null);

        when(clienteGateway.buscarPorId(id)).thenReturn(Optional.of(cliente));

        Cliente executar = buscarClienteUseCase.executar(id);

        assertTrue(executar!=null);
    }
}