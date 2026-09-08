package com.neogym.application.port.out;

import com.neogym.application.dto.request.GerarTreinoIARequest;
import com.neogym.application.dto.response.TreinoIAResponse;

public interface GeradorTreinoIAPort {

    TreinoIAResponse gerar(GerarTreinoIARequest request);
}