package br.com.fiap.tech_challenge_4a_fase_cliente.core.exception;

public class ClienteNaoEncontradoException extends RuntimeException {
    public ClienteNaoEncontradoException(Long id) {
        super("Cliente com ID " + id + " não encontrado.");
    }

    public ClienteNaoEncontradoException(String cpf) {
        super("Cliente com CPF " + cpf + " não encontrado.");
    }
}
