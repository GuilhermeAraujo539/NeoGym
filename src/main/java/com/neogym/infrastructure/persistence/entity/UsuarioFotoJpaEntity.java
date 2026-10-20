package com.neogym.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuario_foto")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioFotoJpaEntity {

    @Id
    @Column(name = "usuario_id")
    private Long usuarioId;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "dados", nullable = false)
    private byte[] dados;

    @Column(name = "content_type", nullable = false, length = 100)
    private String contentType;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;
}