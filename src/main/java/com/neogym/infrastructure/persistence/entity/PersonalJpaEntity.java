package com.neogym.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "personal")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class PersonalJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private UsuarioJpaEntity usuario;

    @Column(nullable = false, length = 20)
    private String cref;

    @Column(name = "estado_cref", nullable = false, length = 2)
    private String estadoCref;

    @Column(name = "status_cref", nullable = false, length = 20)
    private String statusCref;
}
