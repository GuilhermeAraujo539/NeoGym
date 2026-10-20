package com.neogym.application.dto.response;

import java.time.LocalDateTime;

public record CredencialAdminResponse(
        Long id,
        String nomeUsuario,
        String emailUsuario,
        String tipo,
        String status,
        String arquivoUrl,
        LocalDateTime criadoEm,
        LocalDateTime avaliadoEm,
        String observacaoAdmin
) {}