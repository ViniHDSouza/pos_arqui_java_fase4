package br.com.fiap.mspedidoreciver.adapter.controller.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PedidoReciverRequestDTO {
    private Long id;
    private Long clienteId;
    private String numeroCartao;
    private String status;
    private LocalDateTime dataCriacao;
}
