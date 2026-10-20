package com.neogym.domain.entity;

import com.neogym.domain.enums.StatusAgendamento;
import com.neogym.domain.enums.TipoProfissionalAgendamento;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Agendamento {

    private Long id;

    private Long alunoId;

    private Long personalId;

    private Long nutricionistaId;

    private String titulo;

    private String descricao;

    private LocalDateTime dataHora;

    private StatusAgendamento status;

    private LocalDateTime criadoEm;

    private TipoProfissionalAgendamento tipoProfissional;

    public Agendamento(
            Long id,
            Long alunoId,
            Long personalId,
            Long nutricionistaId,
            String titulo,
            String descricao,
            LocalDateTime dataHora,
            StatusAgendamento status,
            LocalDateTime criadoEm,
            TipoProfissionalAgendamento tipoProfissional
    ) {
        this.id = id;
        this.alunoId = alunoId;
        this.personalId = personalId;
        this.nutricionistaId = nutricionistaId;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataHora = dataHora;
        this.status = status;
        this.criadoEm = criadoEm;
        this.tipoProfissional = tipoProfissional;
    }

    public static Agendamento criar(
            Long alunoId,
            Long personalId,
            Long nutricionistaId,
            String titulo,
            String descricao,
            LocalDateTime dataHora,
            TipoProfissionalAgendamento tipoProfissional
    ) {
        return new Agendamento(
                null,
                alunoId,
                personalId,
                nutricionistaId,
                titulo,
                descricao,
                dataHora,
                StatusAgendamento.AGENDADA,
                LocalDateTime.now(),
                tipoProfissional
        );
    }
}