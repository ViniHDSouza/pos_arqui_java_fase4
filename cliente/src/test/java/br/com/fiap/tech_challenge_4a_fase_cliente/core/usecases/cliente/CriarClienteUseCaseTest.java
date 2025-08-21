package br.com.fiap.tech_challenge_4a_fase_cliente.core.usecases.cliente;

import br.com.fiap.tech_challenge_4a_fase_cliente.TechChallenge4aFaseClienteApplication;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.domain.entities.cliente.Cliente;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.domain.entities.endereco.Endereco;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.gateways.ClienteGateway;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = TechChallenge4aFaseClienteApplication.class)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes Unitários do Caso de Uso de Criação de Cliente")
class CriarClienteUseCaseTest {

    private ClienteGateway clienteGateway;
    private CriarClienteUseCase criarClienteUseCase;

    @BeforeEach
    void setUp() {
        clienteGateway = mock(ClienteGateway.class);
        criarClienteUseCase = new CriarClienteUseCase(clienteGateway);
    }

    @Test
    @DisplayName("Deve criar um novo cliente com sucesso")
    void deveCriarClienteComSucesso() {
        Endereco endereco = new Endereco("Rua Teste", "123", "Apto 1", "Bairro", "Cidade", "SP", "01.234-567");
        Cliente cliente = new Cliente(null, "Maria", "123.456.789-10", LocalDate.of(1995, 5, 20), endereco);
        Cliente clienteSalvo = new Cliente(1L, "Maria", "123.456.789-10", LocalDate.of(1995, 5, 20), endereco);

        when(clienteGateway.salvar(cliente)).thenReturn(clienteSalvo);

        Cliente resultado = criarClienteUseCase.executar(cliente);

        assertNotNull(resultado);
        assertEquals(clienteSalvo.getId(), resultado.getId());
        assertEquals(clienteSalvo.getNome(), resultado.getNome());
        assertEquals(clienteSalvo.getCpf(), resultado.getCpf());
        assertEquals(clienteSalvo.getDataNascimento(), resultado.getDataNascimento());
        assertEquals(clienteSalvo.getEndereco(), resultado.getEndereco());
        verify(clienteGateway).salvar(cliente);
    }
}