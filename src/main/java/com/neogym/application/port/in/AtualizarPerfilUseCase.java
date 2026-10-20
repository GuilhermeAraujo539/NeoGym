package com.neogym.application.port.in;

import com.neogym.application.dto.request.AtualizarPerfilRequest;
import com.neogym.application.dto.response.PerfilResponse;

public interface AtualizarPerfilUseCase {

    PerfilResponse executar(
            Long usuarioId,
            AtualizarPerfilRequest request
    );
}