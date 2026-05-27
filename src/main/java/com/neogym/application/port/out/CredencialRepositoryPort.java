package com.neogym.application.port.out;

import com.neogym.domain.entity.Credencial;
import com.neogym.domain.enums.StatusCredencial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CredencialRepositoryPort {

    Credencial salvar(Credencial credencial);

    Optional<Credencial> buscarPorId(Long id);

    Optional<Credencial> buscarPorUsuarioId(Long usuarioId);

    Page<Credencial> listarPorStatus(StatusCredencial status, Pageable pageable);

    Page<Credencial> listarTodas(Pageable pageable);

    long contarPorStatus(StatusCredencial status);

    org.springframework.data.domain.Page<com.neogym.domain.entity.Credencial> listarPorUsuarioId(
            Long usuarioId,
            org.springframework.data.domain.Pageable pageable);
}
