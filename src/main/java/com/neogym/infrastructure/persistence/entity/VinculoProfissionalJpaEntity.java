package com.neogym.infrastructure.persistence.entity;

import com.neogym.domain.enums.StatusVinculo;
import com.neogym.domain.enums.TipoUsuario;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "vinculo_profissional",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_vinculo",
                        columnNames = {
                                "aluno_id",
                                "profissional_usuario_id",
                                "tipo_profissional"
                        }
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VinculoProfissionalJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private AlunoJpaEntity aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profissional_usuario_id", nullable = false)
    private UsuarioJpaEntity profissional;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(
            name = "tipo_profissional",
            nullable = false,
            columnDefinition = "tipo_usuario"
    )
    private TipoUsuario tipoProfissional;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusVinculo status;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
}