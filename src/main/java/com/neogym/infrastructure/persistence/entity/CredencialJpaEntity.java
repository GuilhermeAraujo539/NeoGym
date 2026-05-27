package com.neogym.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "credencial")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CredencialJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioJpaEntity usuario;

    @Column(nullable = false, length = 10)
    private String tipo;

    @Column(name = "arquivo_url", length = 255)
    private String arquivoUrl;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "observacao_admin", length = 500)
    private String observacaoAdmin;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "avaliado_em")
    private LocalDateTime avaliadoEm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avaliado_por_id")
    private UsuarioJpaEntity avaliadoPor;
}
