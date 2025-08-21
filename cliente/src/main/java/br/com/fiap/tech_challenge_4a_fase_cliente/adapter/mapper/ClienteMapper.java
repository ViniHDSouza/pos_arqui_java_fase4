package br.com.fiap.tech_challenge_4a_fase_cliente.adapter.mapper;

import br.com.fiap.tech_challenge_4a_fase_cliente.adapter.controller.request.ClienteRequestDto;
import br.com.fiap.tech_challenge_4a_fase_cliente.adapter.controller.request.ClienteRequestDto.EnderecoRequestDto;
import br.com.fiap.tech_challenge_4a_fase_cliente.adapter.controller.response.ClienteResponseDto;
import br.com.fiap.tech_challenge_4a_fase_cliente.adapter.controller.response.ClienteResponseDto.EnderecoResponseDto;
import br.com.fiap.tech_challenge_4a_fase_cliente.adapter.persistence.entity.ClienteEntity;
import br.com.fiap.tech_challenge_4a_fase_cliente.adapter.persistence.entity.EnderecoEntity;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.domain.entities.cliente.Cliente;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.domain.entities.endereco.Endereco;

public class ClienteMapper {

    public ClienteEntity toClienteEntity(Cliente cliente) {
        if (cliente == null) return null;
        return new ClienteEntity(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getDataNascimento(),
                cliente.getEndereco()
        );
    }

    public Cliente toClientDomain(ClienteEntity entity) {
        if (entity == null) return null;
        Endereco endereco = toEnderecoDomain(entity.getEndereco());
        return new Cliente(
                entity.getId(),
                entity.getNome(),
                entity.getCpf(),
                entity.getDataNascimento(),
                endereco
        );
    }

    public EnderecoEntity toEnderecoEntity(Endereco endereco) {
        if (endereco == null) return null;
        return new EnderecoEntity(
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCep()
        );
    }

    public Endereco toEnderecoDomain(EnderecoEntity entity) {
        if (entity == null) return null;
        return new Endereco(
                entity.getLogradouro(),
                entity.getNumero(),
                entity.getComplemento(),
                entity.getBairro(),
                entity.getCidade(),
                entity.getEstado(),
                entity.getCep()
        );
    }

    public Cliente toClienteDomain(ClienteRequestDto dto) {
        if (dto == null) return null;
        Endereco endereco = toEnderecoDomain(dto.endereco());
        return new Cliente(
                dto.nome(),
                dto.cpf(),
                dto.dataNascimento(),
                endereco
        );
    }

    public ClienteResponseDto toClienteResponseDto(Cliente cliente) {
        if (cliente == null) return null;
        EnderecoResponseDto enderecoDto =
                toEnderecoResponseDto(cliente.getEndereco());
        return new ClienteResponseDto(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf().getDocument(), // Ajuste aqui: supondo que getCpf() retorna um objeto CPF, use getNumero() ou toString()
                cliente.getDataNascimento(),
                enderecoDto
        );
    }

    public ClienteRequestDto toClienteRequestDto(Cliente cliente) {
        if (cliente == null) return null;
        EnderecoResponseDto enderecoDto =
                toEnderecoResponseDto(cliente.getEndereco());
        return new ClienteRequestDto(
                cliente.getNome(),
                cliente.getCpf().getDocument(), // Ajuste aqui: supondo que getCpf() retorna um objeto CPF, use getNumero() ou toString()
                cliente.getDataNascimento(),
                toEnderecoRequestDto(enderecoDto)
        );

    }

    private EnderecoRequestDto toEnderecoRequestDto(EnderecoResponseDto endereco) {
        if (endereco == null) return null;
        return new EnderecoRequestDto(
                endereco.logradouro(),
                endereco.numero(),
                endereco.complemento(),
                endereco.bairro(),
                endereco.cidade(),
                endereco.estado(),
                endereco.cep()
        );
    }

    private EnderecoResponseDto toEnderecoResponseDto(Endereco endereco) {
        if (endereco == null) return null;
        return new EnderecoResponseDto(
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCep()
        );
    }

    private Endereco toEnderecoDomain(EnderecoRequestDto dto) {
        if (dto == null) return null;
        return new Endereco(
                dto.logradouro(),
                dto.numero(),
                dto.complemento(),
                dto.bairro(),
                dto.cidade(),
                dto.estado(),
                dto.cep()
        );
    }


}
