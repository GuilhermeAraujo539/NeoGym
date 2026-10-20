package com.neogym.application.port.in;

import com.neogym.application.dto.response.ProfissionalVinculadoResponse;

import java.util.List;

public interface BuscarProfissionaisVinculadosUseCase {

    List<ProfissionalVinculadoResponse> executar(String email);
}