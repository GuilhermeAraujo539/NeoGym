package com.neogym.application.dto.response;

import com.neogym.domain.enums.StatusCredencial;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PersonalResponse {
    private Long             id;
    private Long             usuarioId;
    private String           nome;
    private String           email;
    private String           cref;
    private String           estadoCref;
    private StatusCredencial statusCref;
    private boolean          ativo;
    private LocalDateTime    criadoEm;
}
