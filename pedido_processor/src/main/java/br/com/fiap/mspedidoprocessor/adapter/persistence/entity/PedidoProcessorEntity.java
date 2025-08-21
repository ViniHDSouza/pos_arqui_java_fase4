package br.com.fiap.mspedidoprocessor.adapter.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pedido_processor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoProcessorEntity {

    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    private String id;

    @Column(name = "pedido_reciver_id")
    private Long pedidoReciverId;

    @Column(name = "cliente_id")
    private Long clienteId;

    @Column(name = "total")
    private BigDecimal total;

    private String status;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @OneToMany(mappedBy = "pedidoProcessor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedidoProcessorEntity> itens;


    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }

//    public PedidoProcessorEntity(PedidoProcessor pedidoProcessor) {
//        String id = pedidoProcessor.getId().toString();
//        if (id == null) {
//            id = UUID.randomUUID().toString();
//        }
//        this.id = id;
//        this.status = pedidoProcessor.getStatus().toString();
//        this.itens = new ArrayList<>();
//        this.pedidoReciverId = pedidoProcessor.getPedidoReciverId();
//        this.clienteId = pedidoProcessor.getClienteId();
//        this.criadoEm = LocalDateTime.now();
//        this.total = pedidoProcessor.getTotal();
//
//
//    }
}