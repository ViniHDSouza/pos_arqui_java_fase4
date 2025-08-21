package br.com.fiap.msestoque.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Estoque {
    private Long id;
    private String sku;
    private Integer quantidade;
    private LocalDateTime atualizadoEm;
}
