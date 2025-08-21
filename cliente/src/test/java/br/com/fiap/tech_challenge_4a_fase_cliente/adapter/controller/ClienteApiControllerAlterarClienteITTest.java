package br.com.fiap.tech_challenge_4a_fase_cliente.adapter.controller;

import br.com.fiap.tech_challenge_4a_fase_cliente.TechChallenge4aFaseClienteApplication;
import br.com.fiap.tech_challenge_4a_fase_cliente.adapter.controller.request.ClienteRequestDto;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.domain.entities.cliente.Cliente;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.domain.entities.endereco.Endereco;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.usecases.cliente.CriarClienteUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TechChallenge4aFaseClienteApplication.class)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes de Integração do Endpoint de Alteração de Cliente")
class ClienteApiControllerAlterarClienteITTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CriarClienteUseCase criarClienteUseCase;

    private ObjectMapper objectMapper;
    private Cliente clienteCriado;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Endereco endereco = new Endereco(
                "Avenida Paulista",
                "1000",
                "Sala 123",
                "Bela Vista",
                "São Paulo",
                "SP",
                "01.310-100"
        );

        clienteCriado = new Cliente(
                null,
                "João da Silva",
                "123.456.789-10",
                LocalDate.of(1990, 1, 1),
                endereco
        );

        clienteCriado = criarClienteUseCase.executar(clienteCriado);
    }

    @Test
    @DisplayName("Deve alterar um cliente com sucesso quando dados são válidos")
    void alterarClienteSucesso() throws Exception {
        ClienteRequestDto.EnderecoRequestDto novoEndereco = new ClienteRequestDto.EnderecoRequestDto(
                "Rua Nova",
                "2000",
                "Apto 456",
                "Centro",
                "Rio de Janeiro",
                "RJ",
                "20.040-100"
        );

        ClienteRequestDto clienteAtualizado = new ClienteRequestDto(
                "João da Silva Atualizado",
                "123.456.789-10",
                LocalDate.of(1990, 1, 1),
                novoEndereco
        );

        mockMvc.perform(put("/clientes/{id}", clienteCriado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João da Silva Atualizado"))
                .andExpect(jsonPath("$.endereco.logradouro").value("Rua Nova"))
                .andExpect(jsonPath("$.endereco.cidade").value("Rio de Janeiro"));
    }

    @Test
    @DisplayName("Deve retornar erro quando tenta alterar um cliente que não existe")
    void alterarClienteNaoEncontrado() throws Exception {
        ClienteRequestDto.EnderecoRequestDto endereco = new ClienteRequestDto.EnderecoRequestDto(
                "Rua Nova",
                "2000",
                "Apto 456",
                "Centro",
                "Rio de Janeiro",
                "RJ",
                "20.040-100"
        );

        ClienteRequestDto clienteAtualizado = new ClienteRequestDto(
                "João da Silva Atualizado",
                "123.456.789-10",
                LocalDate.of(1990, 1, 1),
                endereco
        );

        mockMvc.perform(put("/clientes/{id}", 99999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteAtualizado)))
                .andExpect(status().isNotFound());
    }
}