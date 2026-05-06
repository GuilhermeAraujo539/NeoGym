package com.neogym.infrastructure.web.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private int           status;
    private String        erro;
    private String        mensagem;
    private LocalDateTime timestamp;
    private List<CampoErro> campos;

    @Getter
    @Builder
    public static class CampoErro {
        private String campo;
        private String mensagem;
    }
}
