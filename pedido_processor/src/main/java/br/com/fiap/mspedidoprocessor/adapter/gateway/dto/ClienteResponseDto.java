package br.com.fiap.mspedidoprocessor.adapter.gateway.dto;


import java.time.LocalDate;

public record ClienteResponseDto(
        Long id,
        String nome,
        String cpf,
        LocalDate dataNascimento,
        EnderecoResponseDto endereco
) {
    public record EnderecoResponseDto(
            String logradouro,
            String numero,
            String complemento,
            String bairro,
            String cidade,
            String estado,
            String cep
    ) {}
}