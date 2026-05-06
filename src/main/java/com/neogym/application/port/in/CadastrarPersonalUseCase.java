package com.neogym.application.port.in;

import com.neogym.application.dto.request.CadastrarPersonalRequest;
import com.neogym.application.dto.response.PersonalResponse;

public interface CadastrarPersonalUseCase {
    PersonalResponse executar(CadastrarPersonalRequest request);
}
