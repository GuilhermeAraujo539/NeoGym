package com.neogym.domain.entity;

import com.neogym.domain.enums.StatusCredencial;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Credencial {

    private Long id;
    private Long usuarioId;
    private String tipo;
    private String arquivoUrl;
    private String arquivoIdentidadeUrl;
    private StatusCredencial status;
    private LocalDateTime criadoEm;
}