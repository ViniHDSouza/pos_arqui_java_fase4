package br.com.fiap.msestoque.adapter.controller;

import br.com.fiap.TechChallenge4aFaseEstoqueApplication;
import br.com.fiap.msestoque.adapter.controller.request.BaixaEstoqueRequestDTO;
import br.com.fiap.msestoque.adapter.persistence.entity.EstoqueEntity;
import br.com.fiap.msestoque.adapter.persistence.repository.EstoqueRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TechChallenge4aFaseEstoqueApplication.class)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes de Integração - EstoqueApiController")
class EstoqueApiControllerITTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EstoqueRepository estoqueRepository;

    private static final String SKU_TESTE = "SKU001";
    private static final int QUANTIDADE_INICIAL = 10;

    @BeforeEach
    @DisplayName("Configura o ambiente de teste cadastrando um produto para ser atualizado")
    void setup() {
        EstoqueEntity estoqueEntity = EstoqueEntity.builder()
            .sku(SKU_TESTE)
            .quantidade(QUANTIDADE_INICIAL)
            .atualizadoEm(LocalDateTime.now())
            .build();
            estoqueRepository.save(estoqueEntity);
    }

    @Test
    @DisplayName("Deve realizar baixa no estoque com sucesso")
    void baixaComSucesso() throws Exception {

        BaixaEstoqueRequestDTO baixaEstoqueRequestDTO = new BaixaEstoqueRequestDTO(SKU_TESTE,QUANTIDADE_INICIAL);

        mockMvc.perform(put("/estoque/baixa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(baixaEstoqueRequestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve realizar estorno no estoque com sucesso")
    void estornoComSucesso() throws Exception {

        BaixaEstoqueRequestDTO baixaEstoqueRequestDTO = new BaixaEstoqueRequestDTO(SKU_TESTE,QUANTIDADE_INICIAL);

        mockMvc.perform(put("/estoque/estorno")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(baixaEstoqueRequestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve realizar consulta no estoque com sucesso")
    void consultarEstoqueComSucesso() throws Exception {
        mockMvc.perform(get("/estoque/consulta/{sku}", SKU_TESTE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value(SKU_TESTE))
                .andExpect(jsonPath("$.quantidade").value(QUANTIDADE_INICIAL));
    }
    
    @Test
    @DisplayName("Deve retornar 404 ao tentar estornar com SKU nulo")
    void estornoComSKUNula() throws Exception {
        BaixaEstoqueRequestDTO request = new BaixaEstoqueRequestDTO(null, 0);

        mockMvc.perform(put("/estoque/estorno")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }
    
    @Test
    @DisplayName("Deve retornar 400 ao tentar estornar com quantidade nula")
    void estornoComQuantidadeNula() throws Exception {
        BaixaEstoqueRequestDTO request = new BaixaEstoqueRequestDTO(SKU_TESTE, null);

        mockMvc.perform(put("/estoque/estorno")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("Deve retornar 400 ao consultar estoque com SKU inexistente")
    void consultarEstoqueInexistente() throws Exception {
        mockMvc.perform(get("/estoque/consulta/{sku}", 100)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}