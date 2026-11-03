package com.neogym.application.port.out;

import com.neogym.domain.entity.FotoGaleriaProfissional;

import java.util.List;
import java.util.Optional;

public interface PersonalFotoRepositoryPort {

    List<FotoGaleriaProfissional> listarPorUsuarioId(Long usuarioId);

    long contarPorUsuarioId(Long usuarioId);

    Optional<FotoGaleriaProfissional> buscarPorIdEUsuarioId(
            Long fotoId,
            Long usuarioId
    );

    FotoGaleriaProfissional salvar(FotoGaleriaProfissional foto);

    void excluir(Long fotoId, Long usuarioId);
}