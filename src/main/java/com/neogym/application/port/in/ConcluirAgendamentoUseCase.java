package com.neogym.application.port.in;

public interface ConcluirAgendamentoUseCase {

    void executar(
            Long usuarioId,
            String tipoUsuario,
            Long agendamentoId
    );
}