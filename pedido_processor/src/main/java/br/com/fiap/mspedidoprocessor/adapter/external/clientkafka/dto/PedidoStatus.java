package br.com.fiap.mspedidoprocessor.adapter.external.clientkafka.dto;

public enum PedidoStatus {
    ABERTO,
    FALHADO,
    FECHADO_SEM_ESTOQUE,
    FECHADO_COM_SUCESSO,
    FECHADO_SEM_CREDITO
}
