package com.neogym.application.dto.response;

import java.util.List;

public record AdminUsuariosPaginaResponse(
        List<AdminUsuarioResponse> conteudo,
        long totalElementos
) {}