package com.neogym.application.dto.response;

import com.neogym.domain.enums.StatusAgendamento;
import com.neogym.domain.enums.TipoProfissionalAgendamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoResponse {

    private Long id;

    private Long alunoId;

    private Long profissionalId;

    private String profissionalNome;

    private TipoProfissionalAgendamento tipoProfissional;

    private String titulo;

    private String descricao;

    private LocalDateTime dataHora;

    private StatusAgendamento status;

    private LocalDateTime criadoEm;
}