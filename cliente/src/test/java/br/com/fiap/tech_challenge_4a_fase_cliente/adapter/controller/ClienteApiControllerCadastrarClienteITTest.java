package br.com.fiap.tech_challenge_4a_fase_cliente.adapter.controller;

import br.com.fiap.tech_challenge_4a_fase_cliente.TechChallenge4aFaseClienteApplication;
import br.com.fiap.tech_challenge_4a_fase_cliente.adapter.controller.request.ClienteRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = TechChallenge4aFaseClienteApplication.class)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes de Integração do Endpoint de Cadastro de Cliente")
class ClienteApiControllerCadastrarClienteITTest {

    @Autowired
    private MockMvc mockMvc;

    ClienteRequestDto.EnderecoRequestDto endereco;
    ClienteRequestDto clienteRequestDto;

    @Test
    @DisplayName("Deve cadastrar um cliente com sucesso quando os dados são válidos")
    void cadastrarClienteSucesso() throws Exception {
        endereco = new ClienteRequestDto.EnderecoRequestDto(
                "Avenida Paulista",
                "1000",
                "Sala 123",
                "Bela Vista",
                "São Paulo",
                "SP",
                "01.310-100"
        );
        clienteRequestDto = new ClienteRequestDto(
                "João da Silva",
                "123.456.789-10",
                LocalDate.of(1990, 1, 1),
                endereco
        );
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João da Silva"))
                .andExpect(jsonPath("$.cpf").value("123.456.789-10"));
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar cadastrar cliente com dados inválidos")
    void cadastrarClienteComDadosInvalidos() throws Exception {
        // Arrange
        endereco = new ClienteRequestDto.EnderecoRequestDto(
                "", 
                "",  
                "Sala 123",
                "",  
                "",  
                "",  
                "01310-100"
        );

        clienteRequestDto = new ClienteRequestDto(
                "",  
                "123.456.789-10",
                LocalDate.of(1990, 1, 1),
                endereco
        );

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar cadastrar cliente com CPF já existente")
    void cadastrarClienteCPFRetetido() throws Exception {
        endereco = new ClienteRequestDto.EnderecoRequestDto(
                "Avenida Paulista",
                "1000",
                "Sala 123",
                "Bela Vista",
                "São Paulo",
                "SP",
                "01.310-100"
        );
        clienteRequestDto = new ClienteRequestDto(
                "João da Silva",
                "123.456.789-10",
                LocalDate.of(1990, 1, 1),
                endereco
        );

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteRequestDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteRequestDto)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Deve retornar erro quando o CPF é inválido")
    void cadastrarClienteCPFErrro() throws Exception {
        endereco = new ClienteRequestDto.EnderecoRequestDto(
                "Avenida Paulista",
                "1000",
                "Sala 123",
                "Bela Vista",
                "São Paulo",
                "SP",
                "01.310-100"
        );
        clienteRequestDto = new ClienteRequestDto(
                "João da Silva",
                "123.456.789-1",
                LocalDate.of(1990, 1, 1),
                endereco
        );
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteRequestDto)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Deve retornar erro quando o CEP é inválido")
    void cadastrarClienteCEPErrro() throws Exception {
        endereco = new ClienteRequestDto.EnderecoRequestDto(
                "Avenida Paulista",
                "1000",
                "Sala 123",
                "Bela Vista",
                "São Paulo",
                "SP",
                "01.310-10"
        );
        clienteRequestDto = new ClienteRequestDto(
                "João da Silva",
                "123.456.789-10",
                LocalDate.of(1990, 1, 1),
                endereco
        );
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteRequestDto)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Deve retornar erro quando o CPF é inválido")
    void cadastrarClienteComCpfInvalido() throws Exception {
        endereco = new ClienteRequestDto.EnderecoRequestDto(
                "Avenida Paulista",
                "1000",
                "Sala 123",
                "Bela Vista",
                "São Paulo",
                "SP",
                "01.310-100"
        );

        clienteRequestDto = new ClienteRequestDto(
                "João da Silva",
                "123.456.789",
                LocalDate.of(1990, 1, 1),
                endereco
        );

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.userMessage").value("CPF inválido"));
    }
}