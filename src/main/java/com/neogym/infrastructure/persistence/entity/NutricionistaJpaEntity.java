package com.neogym.infrastructure.persistence.entity;

import com.neogym.domain.enums.StatusCredencial;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status_crn", columnDefinition = "status_credencial")
    private StatusCredencial statusCrn;
}
