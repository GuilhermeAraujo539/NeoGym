package com.neogym.application.dto.response;

import java.time.LocalDateTime;

public record AdminUsuarioResponse(
        Long id,
        String nome,
        String email,
        String tipo,
        boolean ativo,
        LocalDateTime criadoEm
) {}