package br.com.fiap.mspedidoreciver.core.domain;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {
    private Long id;
    private String sku;
    private Integer quantidade;
}
