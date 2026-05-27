package com.neogym.infrastructure.persistence.mapper;

import com.neogym.domain.entity.Credencial;
import com.neogym.domain.enums.StatusCredencial;
import com.neogym.domain.enums.TipoCredencial;
import com.neogym.infrastructure.persistence.entity.CredencialJpaEntity;
import com.neogym.infrastructure.persistence.entity.UsuarioJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class CredencialMapper {

    public Credencial toDomain(CredencialJpaEntity e) {
        if (e == null) return null;
        return Credencial.builder()
                .id(e.getId())
                .usuarioId(e.getUsuario().getId())
                .tipo(TipoCredencial.valueOf(e.getTipo()))
                .arquivoUrl(e.getArquivoUrl())
                .status(StatusCredencial.valueOf(e.getStatus()))
                .observacaoAdmin(e.getObservacaoAdmin())
                .criadoEm(e.getCriadoEm())
                .avaliadoEm(e.getAvaliadoEm())
                .avaliadoPorId(e.getAvaliadoPor() != null ? e.getAvaliadoPor().getId() : null)
                .build();
    }

    public CredencialJpaEntity toEntity(Credencial d, UsuarioJpaEntity usuario,
                                        UsuarioJpaEntity avaliador) {
        if (d == null) return null;
        return CredencialJpaEntity.builder()
                .id(d.getId())
                .usuario(usuario)
                .tipo(d.getTipo().name())
                .arquivoUrl(d.getArquivoUrl())
                .status(d.getStatus().name())
                .observacaoAdmin(d.getObservacaoAdmin())
                .criadoEm(d.getCriadoEm())
                .avaliadoEm(d.getAvaliadoEm())
                .avaliadoPor(avaliador)
                .build();
    }
}
