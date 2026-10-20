package com.neogym.application.port.out;

import com.neogym.domain.entity.FotoGaleriaProfissional;

import java.util.List;
import java.util.Optional;

public interface PersonalFotoRepositoryPort {

    FotoGaleriaProfissional salvar(FotoGaleriaProfissional foto);

    List<FotoGaleriaProfissional> listarPorPersonalId(Long personalId);

    Optional<FotoGaleriaProfissional> buscarPorIdEPersonalId(
            Long fotoId,
            Long personalId
    );

    long contarPorPersonalId(Long personalId);

    void excluir(Long fotoId);
}