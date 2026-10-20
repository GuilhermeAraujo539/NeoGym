package com.neogym.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FotoGaleriaProfissionalResponse {
    private Long id;
    private String contentType;
    private LocalDateTime criadoEm;
}