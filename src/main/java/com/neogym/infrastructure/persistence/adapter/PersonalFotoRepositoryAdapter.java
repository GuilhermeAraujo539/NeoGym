package com.neogym.infrastructure.persistence.adapter;

import com.neogym.application.port.out.PersonalFotoRepositoryPort;
import com.neogym.domain.entity.FotoGaleriaProfissional;
import com.neogym.infrastructure.persistence.entity.PersonalFotoJpaEntity;
import com.neogym.infrastructure.persistence.repository.PersonalFotoJpaRepository;
import com.neogym.infrastructure.persistence.repository.PersonalJpaRepository;
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
    private final PersonalJpaRepository personalRepository;

    @Override
    public FotoGaleriaProfissional salvar(FotoGaleriaProfissional foto) {
        var personal = personalRepository.findById(foto.getPersonalId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Personal não encontrado")
                );

        var entity = PersonalFotoJpaEntity.builder()
                .id(foto.getId())
                .personal(personal)
                .dados(foto.getDados())
                .contentType(foto.getContentType())
                .criadoEm(foto.getCriadoEm())
                .build();

        return toDomain(fotoRepository.save(entity));
    }

    @Override
    public List<FotoGaleriaProfissional> listarPorPersonalId(
            Long personalId
    ) {
        return fotoRepository
                .findByPersonal_IdOrderByCriadoEmDesc(personalId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<FotoGaleriaProfissional> buscarPorIdEPersonalId(
            Long fotoId,
            Long personalId
    ) {
        return fotoRepository.findByIdAndPersonal_Id(fotoId, personalId)
                .map(this::toDomain);
    }

    @Override
    public long contarPorPersonalId(Long personalId) {
        return fotoRepository.countByPersonal_Id(personalId);
    }

    @Override
    public void excluir(Long fotoId) {
        fotoRepository.deleteById(fotoId);
    }

    private FotoGaleriaProfissional toDomain(
            PersonalFotoJpaEntity entity
    ) {
        return new FotoGaleriaProfissional(
                entity.getId(),
                entity.getPersonal().getId(),
                entity.getDados(),
                entity.getContentType(),
                entity.getCriadoEm()
        );
    }
}