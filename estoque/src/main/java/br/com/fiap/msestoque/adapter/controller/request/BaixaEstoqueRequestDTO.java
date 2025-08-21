package br.com.fiap.msestoque.adapter.controller.request;

public record BaixaEstoqueRequestDTO(
        String sku,
        Integer quantidade
) {}