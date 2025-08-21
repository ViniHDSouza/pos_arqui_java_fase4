package br.com.fiap.tech_challenge_4a_fase_cliente.adapter.mapper;

import br.com.fiap.tech_challenge_4a_fase_cliente.adapter.controller.request.ClienteRequestDto;
import br.com.fiap.tech_challenge_4a_fase_cliente.adapter.controller.response.ClienteResponseDto;
import br.com.fiap.tech_challenge_4a_fase_cliente.adapter.persistence.entity.ClienteEntity;
import br.com.fiap.tech_challenge_4a_fase_cliente.adapter.persistence.entity.EnderecoEntity;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.domain.entities.cliente.Cliente;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.domain.entities.endereco.Endereco;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.domain.vo.CPF;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes Unitários do Mapeador de Cliente")
class ClienteMapperTest {

    private ClienteMapper mapper;
    private Cliente cliente;
    private ClienteEntity clienteEntity;
    private Endereco endereco;
    private EnderecoEntity enderecoEntity;
    private ClienteRequestDto clienteRequestDto;

    @BeforeEach
    void setUp() {
        mapper = new ClienteMapper();

        endereco = new Endereco(
                "Rua Teste",
                "123",
                "Apto 1",
                "Bairro",
                "Cidade",
                "SP",
                "01.234-567"
        );

        enderecoEntity = new EnderecoEntity(
                "Rua Teste",
                "123",
                "Apto 1",
                "Bairro",
                "Cidade",
                "SP",
                "01.234-567"
        );
        CPF cpf = new CPF("123.456.789-10");

        cliente = new Cliente(
                1L,
                "João Silva",
                cpf.getDocument(),
                LocalDate.of(1990, 1, 1),
                endereco
        );

        clienteEntity = new ClienteEntity(
                1L,
                "João Silva",
                cpf,
                LocalDate.of(1990, 1, 1),
                endereco
        );

        clienteRequestDto = new ClienteRequestDto(
                "João Silva",
                cpf.getDocument(),
                LocalDate.of(1990, 1, 1),
                new ClienteRequestDto.EnderecoRequestDto(
                        "Rua Teste",
                        "123",
                        "Apto 1",
                        "Bairro",
                        "Cidade",
                        "SP",
                        "01.234-567"
                )
        );
    }


    @Test
    @DisplayName("Deve converter um Cliente do domínio para uma entidade ClienteEntity")
    void toClienteEntity() {
        ClienteEntity result = mapper.toClienteEntity(cliente);
        assertNotNull(result);
        assertEquals(cliente.getId(), result.getId());
        assertEquals(cliente.getNome(), result.getNome());
        assertEquals(cliente.getCpf().getDocument(), result.getCpf());
        assertEquals(cliente.getDataNascimento(), result.getDataNascimento());
        assertNotNull(result.getEndereco());
    }

    @Test
    @DisplayName("Deve converter uma entidade ClienteEntity para um Cliente do domínio") 
    void toClientDomain() {
        Cliente result = mapper.toClientDomain(clienteEntity);
        assertNotNull(result);
        assertEquals(clienteEntity.getId(), result.getId());
        assertEquals(clienteEntity.getNome(), result.getNome());
        assertEquals(clienteEntity.getCpf(), result.getCpf().getDocument()); 
        assertEquals(clienteEntity.getDataNascimento(), result.getDataNascimento());
        assertNotNull(result.getEndereco());
    }

    @Test
    @DisplayName("Deve converter um Endereco do domínio para uma entidade EnderecoEntity")
    void toEnderecoEntity() {
        EnderecoEntity result = mapper.toEnderecoEntity(endereco);
        assertNotNull(result);
        assertEquals(endereco.getLogradouro(), result.getLogradouro());
        assertEquals(endereco.getNumero(), result.getNumero());
        assertEquals(endereco.getComplemento(), result.getComplemento());
        assertEquals(endereco.getBairro(), result.getBairro());
        assertEquals(endereco.getCidade(), result.getCidade());
        assertEquals(endereco.getEstado(), result.getEstado());
        assertEquals(endereco.getCep(), result.getCep());
    }

    @Test
    @DisplayName("Deve converter uma entidade EnderecoEntity para um Endereco do domínio")
    void toEnderecoDomain() {
        Endereco result = mapper.toEnderecoDomain(enderecoEntity);
        assertNotNull(result);
        assertEquals(enderecoEntity.getLogradouro(), result.getLogradouro());
        assertEquals(enderecoEntity.getNumero(), result.getNumero());
        assertEquals(enderecoEntity.getComplemento(), result.getComplemento());
        assertEquals(enderecoEntity.getBairro(), result.getBairro());
        assertEquals(enderecoEntity.getCidade(), result.getCidade());
        assertEquals(enderecoEntity.getEstado(), result.getEstado());
        assertEquals(enderecoEntity.getCep(), result.getCep());
    }

    @Test
    @DisplayName("Deve converter um ClienteRequestDto para um Cliente do domínio")
    void toClienteDomain() {
        Cliente result = mapper.toClienteDomain(clienteRequestDto);
        assertNotNull(result);
        assertEquals(clienteRequestDto.nome(), result.getNome());
        assertEquals(clienteRequestDto.cpf(), result.getCpf().getDocument());
        assertEquals(clienteRequestDto.dataNascimento(), result.getDataNascimento());
        assertNotNull(result.getEndereco());
    }

    @Test
    @DisplayName("Deve converter um Cliente do domínio para um ClienteResponseDto")
    void toClienteResponseDto() {
        ClienteResponseDto result = mapper.toClienteResponseDto(cliente);
        assertNotNull(result);
        assertEquals(cliente.getId(), result.id());
        assertEquals(cliente.getNome(), result.nome());
        assertEquals(cliente.getCpf().getDocument(), result.cpf());
        assertEquals(cliente.getDataNascimento(), result.dataNascimento());
        assertNotNull(result.endereco());
    }

    @Test
    @DisplayName("Deve converter um Cliente do domínio para um ClienteRequestDto")
    void toClienteRequestDto() {
        ClienteRequestDto result = mapper.toClienteRequestDto(cliente);
        assertNotNull(result);
        assertEquals(cliente.getNome(), result.nome());
        assertEquals(cliente.getCpf().getDocument(), result.cpf());
        assertEquals(cliente.getDataNascimento(), result.dataNascimento());
        assertNotNull(result.endereco());
    }

    @Test
    @DisplayName("Deve retornar null quando o input for null em todos os métodos de mapeamento")
    void shouldReturnNullWhenInputIsNull() {
        assertNull(mapper.toClienteEntity(null));
        assertNull(mapper.toClientDomain((ClienteEntity) null));
        assertNull(mapper.toEnderecoEntity(null));
        assertNull(mapper.toEnderecoDomain((EnderecoEntity) null));
        assertNull(mapper.toClienteDomain((ClienteRequestDto) null));
        assertNull(mapper.toClienteResponseDto(null));
        assertNull(mapper.toClienteRequestDto(null));
    }
}