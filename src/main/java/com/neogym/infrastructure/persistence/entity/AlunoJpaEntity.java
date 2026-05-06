package com.neogym.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "aluno")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class AlunoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private UsuarioJpaEntity usuario;

    @Column(name = "academia_id")
    private Long academiaId;

    @Column(precision = 5, scale = 2)
    private BigDecimal peso;

    @Column(precision = 4, scale = 2)
    private BigDecimal altura;

    @Column(name = "meta_agua_ml")
    private Integer metaAguaMl;
}
