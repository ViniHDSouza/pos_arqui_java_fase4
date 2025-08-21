package br.com.fiap.mspedidoprocessor.adapter.external.estoqueservice.dto;

public record BaixaEstoqueRequestDTO(
        String sku,
        int quantidade
) {}