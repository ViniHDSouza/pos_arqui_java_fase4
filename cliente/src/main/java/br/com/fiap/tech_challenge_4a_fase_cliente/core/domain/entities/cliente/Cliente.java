package br.com.fiap.tech_challenge_4a_fase_cliente.core.domain.entities.cliente;

import br.com.fiap.tech_challenge_4a_fase_cliente.core.domain.entities.endereco.Endereco;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.domain.vo.CPF;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Cliente {

    private Long id;
    private String nome;
    private CPF cpf;
    private LocalDate dataNascimento;
    private Endereco endereco;


    public Cliente(){}

    public Cliente(Long id, String nome, String cpf, LocalDate dataNascimento, Endereco endereco) {
        this(nome, cpf, dataNascimento, endereco);
        this.id = id;
    }

    public Cliente(String nome, String cpf, LocalDate dataNascimento, Endereco endereco) {
        this.nome = nome;
        this.cpf = new CPF(cpf);
        this.dataNascimento = dataNascimento;
        this.endereco = endereco;
    }
}
