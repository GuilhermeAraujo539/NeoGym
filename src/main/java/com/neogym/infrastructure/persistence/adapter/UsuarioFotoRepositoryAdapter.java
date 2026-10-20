package com.neogym.infrastructure.persistence.adapter;

import com.neogym.application.port.out.FotoUsuario;
import com.neogym.application.port.out.UsuarioFotoRepositoryPort;
import com.neogym.infrastructure.persistence.entity.UsuarioFotoJpaEntity;
import com.neogym.infrastructure.persistence.repository.UsuarioFotoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsuarioFotoRepositoryAdapter
        implements UsuarioFotoRepositoryPort {

    private final UsuarioFotoJpaRepository repository;

    @Override
    public void salvar(
            Long usuarioId,
            byte[] dados,
            String contentType
    ) {

        UsuarioFotoJpaEntity entity =
                UsuarioFotoJpaEntity.builder()
                        .usuarioId(usuarioId)
                        .dados(dados)
                        .contentType(contentType)
                        .atualizadoEm(LocalDateTime.now())
                        .build();

        repository.save(entity);
    }

    @Override
    public Optional<FotoUsuario> buscar(Long usuarioId) {

        return repository.findById(usuarioId)
                .map(entity ->
                        new FotoUsuario(
                                entity.getDados(),
                                entity.getContentType()
                        )
                );
    }
}