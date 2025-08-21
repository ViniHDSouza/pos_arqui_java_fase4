package br.com.fiap.tech_challenge_4a_fase_cliente.core.domain.vo;

import br.com.fiap.tech_challenge_4a_fase_cliente.core.exception.CPFInvalidoException;

public class CPF {

    private String document;

    public CPF(String document) {
        if (document == null || !document.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            throw new CPFInvalidoException();
        }
        this.document = document;
    }

    @Override
    public String toString() {
        return this.document;
    }

    public String getDocument() {
        return document;
    }
}
