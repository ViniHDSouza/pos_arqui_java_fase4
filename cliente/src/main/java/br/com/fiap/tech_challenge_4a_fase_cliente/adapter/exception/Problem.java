package br.com.fiap.tech_challenge_4a_fase_cliente.adapter.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Problem {

    private Integer status;
    private String type;
    private String title;
    private String detail;

    private String userMessage;
    private LocalDateTime timestamp;
    private List<Field> fields;

    @Getter
    @Builder
    public static class Field{
        private String name;
        private String userMessage;
    }

}