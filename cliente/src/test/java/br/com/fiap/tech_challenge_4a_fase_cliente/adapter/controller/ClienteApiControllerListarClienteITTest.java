package br.com.fiap.tech_challenge_4a_fase_cliente.adapter.controller;

import br.com.fiap.tech_challenge_4a_fase_cliente.TechChallenge4aFaseClienteApplication;
import br.com.fiap.tech_challenge_4a_fase_cliente.adapter.persistence.repository.ClienteRepository;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.domain.entities.cliente.Cliente;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.domain.entities.endereco.Endereco;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.usecases.cliente.CriarClienteUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = TechChallenge4aFaseClienteApplication.class)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes de Integração do Endpoint de Listagem de Clientes")
class ClienteApiControllerListarClienteITTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CriarClienteUseCase criarUsuarioUseCase;

    @Autowired
    private ClienteRepository clienteRepository;

    @BeforeEach
    void setUp() {
        clienteRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve listar todos os clientes cadastrados")
    void listarClientes() throws Exception {        
        Endereco endereco1 = new Endereco("Rua A", "123", "", "São Paulo", "Sao Paulo", "SP", "12.345-789");
        Cliente cliente1 = new Cliente(null, "João", "123.456.789-10", LocalDate.now(), endereco1);
        Endereco endereco2 = new Endereco("Rua B", "123", "", "São Paulo", "SP", "", "12.346-789");
        Cliente cliente2 = new Cliente(null, "Maria", "987.654.321-00", LocalDate.now(),endereco2);
        criarUsuarioUseCase.executar(cliente1);
        criarUsuarioUseCase.executar(cliente2);
        mockMvc.perform(get("/clientes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }
}