package br.com.fiap.mspedidoreciver.core.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long clienteId;
    private String numeroCartao;
    private List<ItemPedido> itens;
    private String status;
    private LocalDateTime dataCriacao;
}
