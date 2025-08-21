package br.com.fiap.tech_challenge_4a_fase_cliente.core.domain.vo;

import br.com.fiap.tech_challenge_4a_fase_cliente.core.exception.CEPInvalidoException;

public class CEP {

    private String document;

    public CEP(String document) {
        if (document == null || !document.matches("\\d{2}.\\d{3}-\\d{3}")) {
            throw new CEPInvalidoException();
        }
        this.document = document;
    }

    public String getDocument() {
        return document;
    }
}
