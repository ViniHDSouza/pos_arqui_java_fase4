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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TechChallenge4aFaseClienteApplication.class)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes de Integração do Endpoint de Deleção de Cliente")
class ClienteApiControllerDeletarClienteITTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CriarClienteUseCase criarUsuarioUseCase;

    @Autowired
    private ClienteRepository clienteRepository;

    private Cliente clienteCriado;

    @BeforeEach
    void setUp() {
        clienteRepository.deleteAll();

        Endereco endereco = new Endereco(
                "Avenida Paulista",
                "1000",
                "Sala 123",
                "Bela Vista",
                "São Paulo",
                "SP",
                "01.310-100"
        );

        Cliente cliente = new Cliente(
                null,
                "João da Silva",
                "123.456.789-10",
                LocalDate.of(1990, 1, 1),
                endereco
        );

        clienteCriado = criarUsuarioUseCase.executar(cliente);
    }

    @Test
    @DisplayName("Deve deletar um cliente com sucesso quando ID existe")
    void deletarClienteSucesso() throws Exception {
        mockMvc.perform(delete("/clientes/{id}", clienteCriado.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar deletar um cliente que não existe")
    void deletarClienteNaoEncontrado() throws Exception {
        mockMvc.perform(delete("/clientes/{id}", 99999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}