package br.com.fiap.tech_challenge_4a_fase_cliente.adapter.persistence.entity;

import br.com.fiap.tech_challenge_4a_fase_cliente.core.domain.entities.endereco.Endereco;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.domain.vo.CPF;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "clientes")
public class ClienteEntity {

    public ClienteEntity() {

    }

    public ClienteEntity(Long id, String nome, CPF cpf, LocalDate dataNascimento, Endereco endereco) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf.getDocument();
        this.dataNascimento = dataNascimento;
        this.endereco = new EnderecoEntity(endereco);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false, unique = true, length = 14)
    private String cpf;
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;
    @Embedded
    private EnderecoEntity endereco;


}
