package br.com.fiap.mspedidoprocessor.core.domain;

public enum PedidoStatus {
    ABERTO,
    APROVADO,
    PROCESSANDO,
    RECUSADO,
    FECHADO_SEM_ESTOQUE,
    FALHA_PAGAMENTO,
    FALHA_CLIENTE_NAOENCONTRADO,
    FALHA_PRODUTO_NAOENCONTRADO
}
