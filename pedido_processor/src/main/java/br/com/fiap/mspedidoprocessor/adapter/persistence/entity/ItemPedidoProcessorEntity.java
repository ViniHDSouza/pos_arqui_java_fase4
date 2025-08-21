package br.com.fiap.mspedidoprocessor.adapter.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "item_pedido_processor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPedidoProcessorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "produto_id")
    private Long produtoId;

    @Column(name = "sku")
    private String sku;

    @Column(name = "quantidade")
    private Integer quantidade;

    @Column(name = "preco_unitario")
    private BigDecimal precoUnitario;

    @Column(name = "preco_total")
    private BigDecimal precoTotal;

    @ManyToOne
    @JoinColumn(name = "pedidoprocessor_id")
    private PedidoProcessorEntity pedidoProcessor;
}