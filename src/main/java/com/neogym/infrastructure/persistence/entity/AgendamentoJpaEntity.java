package com.neogym.infrastructure.persistence.entity;

import com.neogym.domain.enums.StatusAgendamento;
import com.neogym.domain.enums.TipoProfissionalAgendamento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "agendamento")
@Getter
@NoArgsConstructor
public class AgendamentoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aluno_id", nullable = false)
    private Long alunoId;

    @Column(name = "personal_id")
    private Long personalId;

    @Column(name = "nutricionista_id")
    private Long nutricionistaId;

    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(length = 500)
    private String descricao;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusAgendamento status;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Enumerated(EnumType.STRING)
    @Transient
    private TipoProfissionalAgendamento tipoProfissional;

    public AgendamentoJpaEntity(
            Long alunoId,
            Long personalId,
            Long nutricionistaId,
            String titulo,
            String descricao,
            LocalDateTime dataHora,
            StatusAgendamento status,
            LocalDateTime criadoEm
    ) {
        this.alunoId = alunoId;
        this.personalId = personalId;
        this.nutricionistaId = nutricionistaId;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataHora = dataHora;
        this.status = status;
        this.criadoEm = criadoEm;
    }
}