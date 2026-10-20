package com.neogym.application.port.in;

import com.neogym.application.dto.request.AtualizarPerfilProfissionalRequest;
import com.neogym.application.dto.response.PerfilProfissionalResponse;

public interface GerenciarPerfilProfissionalUseCase {

    PerfilProfissionalResponse buscar(Long usuarioId);

    PerfilProfissionalResponse atualizar(
            Long usuarioId,
            AtualizarPerfilProfissionalRequest request
    );
}