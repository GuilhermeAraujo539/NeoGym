package com.neogym.application.dto.response;

import com.neogym.domain.enums.StatusCredencial;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NutricionistaResponse {
    private Long             id;
    private Long             usuarioId;
    private String           nome;
    private String           email;
    private String           crn;
    private String           estadoCrn;
    private StatusCredencial statusCrn;
    private boolean          ativo;
    private LocalDateTime    criadoEm;
}
