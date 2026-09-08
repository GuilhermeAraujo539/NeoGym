package com.neogym.application.port.in;

import com.neogym.application.dto.request.GerarTreinoIARequest;
import com.neogym.application.dto.response.TreinoIAResponse;

public interface GerarTreinoIAUseCase {

    TreinoIAResponse executar(
            Long usuarioId,
            GerarTreinoIARequest request
    );
}