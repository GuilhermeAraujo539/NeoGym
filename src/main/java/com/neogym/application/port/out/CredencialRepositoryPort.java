package com.neogym.application.port.out;

import com.neogym.domain.entity.Credencial;

import java.util.Optional;

public interface CredencialRepositoryPort {

    void salvar(Credencial credencial);

    Optional<String> buscarNomeArquivo(Long id, String tipo);
}