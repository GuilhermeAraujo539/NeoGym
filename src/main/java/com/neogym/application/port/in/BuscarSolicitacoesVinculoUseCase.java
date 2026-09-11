package com.neogym.application.port.in;

import com.neogym.domain.entity.VinculoProfissional;

import java.util.List;

public interface BuscarSolicitacoesVinculoUseCase {

    List<VinculoProfissional> executar(String email);
}