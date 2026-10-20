package com.neogym.application.dto.request;

import com.neogym.domain.enums.TipoProfissionalAgendamento;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CriarAgendamentoRequest {

    /*
     * Necessário quando o solicitante é Personal
     * ou Nutricionista.
     *
     * Quando o solicitante é ALUNO,
     * o backend descobre o aluno através do JWT.
     */
    private Long alunoId;

    /*
     * ID do profissional que será atendido.
     *
     * É utilizado principalmente quando
     * quem está criando o agendamento é o aluno.
     */
    private Long profissionalId;

    /*
     * PERSONAL ou NUTRICIONISTA
     */
    private TipoProfissionalAgendamento tipoProfissional;

    @NotBlank
    private String titulo;

    private String descricao;

    @NotNull
    @FutureOrPresent
    private LocalDateTime dataHora;
}