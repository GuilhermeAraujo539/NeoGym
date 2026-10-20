package com.neogym.application.dto.response;

import java.util.List;

public record CredenciaisAdminPaginaResponse(
        List<CredencialAdminResponse> conteudo,
        long totalElementos
) {}