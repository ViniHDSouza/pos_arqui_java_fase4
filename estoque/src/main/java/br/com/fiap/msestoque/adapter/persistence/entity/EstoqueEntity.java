package br.com.fiap.msestoque.adapter.persistence.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "estoque", uniqueConstraints = {
        @UniqueConstraint(name = "uk_produto_sku", columnNames = {"sku"})
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class EstoqueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String sku;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private LocalDateTime atualizadoEm;

}