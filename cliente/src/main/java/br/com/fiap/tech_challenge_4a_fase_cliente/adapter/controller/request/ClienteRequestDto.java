package br.com.fiap.tech_challenge_4a_fase_cliente.adapter.controller.request;

import java.time.LocalDate;

public record ClienteRequestDto(
        String nome,
        String cpf,
        LocalDate dataNascimento,
        EnderecoRequestDto endereco
) {
    public record EnderecoRequestDto(
            String logradouro,
            String numero,
            String complemento,
            String bairro,
            String cidade,
            String estado,
            String cep
    ) {}
}