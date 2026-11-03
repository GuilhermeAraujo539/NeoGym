package com.neogym.infrastructure.persistence.adapter;

import com.neogym.application.port.out.PersonalFotoRepositoryPort;
import com.neogym.domain.entity.FotoGaleriaProfissional;
import com.neogym.infrastructure.persistence.entity.PersonalFotoJpaEntity;
import com.neogym.infrastructure.persistence.repository.PersonalFotoJpaRepository;
import com.neogym.infrastructure.persistence.repository.UsuarioJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PersonalFotoRepositoryAdapter
        implements PersonalFotoRepositoryPort {

    private final PersonalFotoJpaRepository fotoRepository;
    private final UsuarioJpaRepository usuarioRepository;

    @Override
    public FotoGaleriaProfissional salvar(FotoGaleriaProfissional foto) {
        var usuario = usuarioRepository.findById(foto.getUsuarioId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Usuário proprietário da galeria não encontrado."
                        )
                );

        var entity = PersonalFotoJpaEntity.builder()
                .id(foto.getId())
                .usuario(usuario)
                .dados(foto.getDados())
                .contentType(foto.getContentType())
                .criadoEm(foto.getCriadoEm())
                .build();

        return toDomain(fotoRepository.save(entity));
    }

    @Override
    public List<FotoGaleriaProfissional> listarPorUsuarioId(
            Long usuarioId) {

        return fotoRepository
                .findByUsuario_IdOrderByCriadoEmDesc(usuarioId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<FotoGaleriaProfissional> buscarPorIdEUsuarioId(
            Long fotoId,
            Long usuarioId) {

        return fotoRepository
                .findByIdAndUsuario_Id(fotoId, usuarioId)
                .map(this::toDomain);
    }

    @Override
    public long contarPorUsuarioId(Long usuarioId) {
        return fotoRepository.countByUsuario_Id(usuarioId);
    }

    @Override
    public void excluir(Long fotoId, Long usuarioId) {
        fotoRepository.deleteByIdAndUsuario_Id(fotoId, usuarioId);
    }

    private FotoGaleriaProfissional toDomain(
            PersonalFotoJpaEntity entity) {

        return new FotoGaleriaProfissional(
                entity.getId(),
                entity.getUsuario().getId(),
                entity.getDados(),
                entity.getContentType(),
                entity.getCriadoEm()
        );
    }
}