package br.com.fiap.mspedidoreciver.adapter.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ProblemTypeTest {

    @Test
    void deveRetornarUriECorretamente() {
        assertEquals("http://www.fiap.com.br/recurso-nao-encontrado", ProblemType.RECURSO_NAO_ENCONTRADO.getUri());
        assertEquals("Recurso não encontrado", ProblemType.RECURSO_NAO_ENCONTRADO.getTitle());

        assertEquals("http://www.fiap.com.br/entidade-em-uso", ProblemType.ENTIDADE_EM_USO.getUri());
        assertEquals("Entidade em uso", ProblemType.ENTIDADE_EM_USO.getTitle());

        assertEquals("http://www.fiap.com.br/erro-negocio", ProblemType.ERRO_NEGOCIO.getUri());
        assertEquals("Violação de regra de negócio", ProblemType.ERRO_NEGOCIO.getTitle());

        assertEquals("http://www.fiap.com.br/dados-invalidos", ProblemType.DADOS_INVALIDOS.getUri());
        assertEquals("Dados inválidos", ProblemType.DADOS_INVALIDOS.getTitle());

        assertEquals("http://www.fiap.com.br/cpf-ja-cadastrado", ProblemType.CPF_JA_CADASTRADO.getUri());
        assertEquals("CPF Já Cadastrado", ProblemType.CPF_JA_CADASTRADO.getTitle());
    }

    @Test
    void deveTerTodosOsEnumsCadastrados() {
        assertEquals(12, ProblemType.values().length);
    }
}
