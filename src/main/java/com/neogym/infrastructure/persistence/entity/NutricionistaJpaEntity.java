package com.neogym.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nutricionista")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class NutricionistaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private UsuarioJpaEntity usuario;

    @Column(nullable = false, length = 20)
    private String crn;

    @Column(name = "estado_crn", nullable = false, length = 2)
    private String estadoCrn;

    @Column(name = "status_crn", nullable = false, length = 20)
    private String statusCrn;
}
