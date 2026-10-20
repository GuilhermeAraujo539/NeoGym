package com.neogym.application.port.in;

import com.neogym.application.dto.response.FotoGaleriaProfissionalConteudoResponse;
import com.neogym.application.dto.response.FotoGaleriaProfissionalResponse;

import java.util.List;

public interface GerenciarGaleriaProfissionalUseCase {

    List<FotoGaleriaProfissionalResponse> listar(Long usuarioId);

    FotoGaleriaProfissionalResponse adicionar(
            Long usuarioId,
            byte[] dados,
            String contentType
    );

    FotoGaleriaProfissionalConteudoResponse obterConteudo(
            Long usuarioId,
            Long fotoId
    );

    void excluir(Long usuarioId, Long fotoId);
}