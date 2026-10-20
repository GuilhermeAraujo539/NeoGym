package com.neogym.application.port.in;

import com.neogym.application.dto.response.AgendamentoResponse;

import java.util.List;

public interface ListarMeusAgendamentosUseCase {

    List<AgendamentoResponse> executar(
            Long usuarioId,
            String tipoUsuario
    );
}