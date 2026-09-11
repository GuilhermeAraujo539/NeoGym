package com.neogym.application.dto.response;

import com.neogym.domain.enums.StatusVinculo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StatusVinculoResponse {

    private boolean existe;
    private Long vinculoId;
    private StatusVinculo status;
}