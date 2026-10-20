package com.neogym.application.port.in;

import com.neogym.application.dto.response.FichaTreinoResponse;

import java.util.List;

public interface ListarFichasTreinoUseCase {

    List<FichaTreinoResponse> executar(Long alunoId);

    List<FichaTreinoResponse> executarPorUsuarioId(Long usuarioId);

    List<FichaTreinoResponse> executarParaPersonal(
            Long usuarioId,
            Long alunoId
    );
}