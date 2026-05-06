package com.neogym.application.port.in;

import com.neogym.application.dto.request.CadastrarNutricionistaRequest;
import com.neogym.application.dto.response.NutricionistaResponse;

public interface CadastrarNutricionistaUseCase {
    NutricionistaResponse executar(CadastrarNutricionistaRequest request);
}
