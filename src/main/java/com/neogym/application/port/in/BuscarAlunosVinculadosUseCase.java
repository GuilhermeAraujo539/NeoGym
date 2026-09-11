package com.neogym.application.port.in;

import com.neogym.application.dto.response.AlunoVinculadoResponse;

import java.util.List;

public interface BuscarAlunosVinculadosUseCase {

    List<AlunoVinculadoResponse> executar(String email);
}