package com.neogym.application.port.in;

import com.neogym.application.dto.request.CadastrarAlunoRequest;
import com.neogym.application.dto.response.AlunoResponse;

public interface CadastrarAlunoUseCase {
    AlunoResponse executar(CadastrarAlunoRequest request);
}
