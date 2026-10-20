package com.neogym.infrastructure.persistence.entity;

import com.neogym.domain.enums.StatusCredencial;
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

    @Enumerated(EnumType.STRING)
    @org.hibernate.annotations.JdbcTypeCode(
            org.hibernate.type.SqlTypes.NAMED_ENUM
    )
    @Column(
            name = "status_cref",
            nullable = false,
            columnDefinition = "status_credencial"
    )
    private StatusCredencial statusCref;

    @Column(name = "especialidade", nullable = false, length = 120)
    private String especialidade = "";

    @Column(name = "apresentacao", nullable = false, columnDefinition = "text")
    private String apresentacao = "";

    @Column(name = "areas_atuacao", nullable = false, columnDefinition = "text")
    private String areasAtuacao = "";

    @Column(name = "horarios_disponiveis", nullable = false, columnDefinition = "text")
    private String horariosDisponiveis = "";
}
