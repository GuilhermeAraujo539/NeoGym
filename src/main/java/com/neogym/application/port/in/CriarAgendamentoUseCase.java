package com.neogym.application.port.in;

import com.neogym.application.dto.request.CriarAgendamentoRequest;
import com.neogym.application.dto.response.AgendamentoResponse;

public interface CriarAgendamentoUseCase {

    AgendamentoResponse executar(
            Long usuarioId,
            String tipoUsuario,
            CriarAgendamentoRequest request
    );
}