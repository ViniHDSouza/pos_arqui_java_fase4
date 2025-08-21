package br.com.fiap.mspedidoprocessor.adapter.external.estoqueservice.dto;

public class EstoqueDTO {
    public String sku;
    public int quantidade;

    public EstoqueDTO(String sku, int quantidade) {
        this.sku = sku;
        this.quantidade = quantidade;
    }
}