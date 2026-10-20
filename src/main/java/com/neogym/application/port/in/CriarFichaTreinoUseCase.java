package com.neogym.application.port.in;

import com.neogym.application.dto.request.CriarFichaTreinoRequest;
import com.neogym.application.dto.response.FichaTreinoResponse;

public interface CriarFichaTreinoUseCase {

    FichaTreinoResponse executar(
            Long usuarioId,
            CriarFichaTreinoRequest request
    );
}