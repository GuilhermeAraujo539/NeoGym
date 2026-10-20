package com.neogym.application.usecase;

import com.neogym.application.port.in.ExcluirAgendamentoUseCase;
import com.neogym.application.port.out.AgendamentoRepositoryPort;
import com.neogym.application.port.out.AlunoRepositoryPort;
import com.neogym.application.port.out.NutricionistaRepositoryPort;
import com.neogym.application.port.out.PersonalRepositoryPort;
import com.neogym.domain.entity.Agendamento;

import java.util.Objects;

public class ExcluirAgendamentoService
        implements ExcluirAgendamentoUseCase {

    private final AgendamentoRepositoryPort agendamentoRepository;
    private final AlunoRepositoryPort alunoRepository;
    private final PersonalRepositoryPort personalRepository;
    private final NutricionistaRepositoryPort nutricionistaRepository;

    public ExcluirAgendamentoService(
            AgendamentoRepositoryPort agendamentoRepository,
            AlunoRepositoryPort alunoRepository,
            PersonalRepositoryPort personalRepository,
            NutricionistaRepositoryPort nutricionistaRepository
    ) {
        this.agendamentoRepository = agendamentoRepository;
        this.alunoRepository = alunoRepository;
        this.personalRepository = personalRepository;
        this.nutricionistaRepository = nutricionistaRepository;
    }

    @Override
    public void executar(
            Long usuarioId,
            String tipoUsuario,
            Long agendamentoId
    ) {
        Agendamento agendamento = agendamentoRepository
                .buscarPorId(agendamentoId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Agendamento não encontrado"
                        )
                );

        boolean autorizado;

        switch (tipoUsuario) {
            case "ROLE_ALUNO" -> {
                var aluno = alunoRepository
                        .buscarPorUsuarioId(usuarioId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Aluno não encontrado"
                                )
                        );

                autorizado = Objects.equals(
                        agendamento.getAlunoId(),
                        aluno.getId()
                );
            }

            case "ROLE_PERSONAL" -> {
                var personal = personalRepository
                        .buscarPorUsuarioId(usuarioId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Personal não encontrado"
                                )
                        );

                autorizado = Objects.equals(
                        agendamento.getPersonalId(),
                        personal.getId()
                );
            }

            case "ROLE_NUTRICIONISTA" -> {
                var nutricionista = nutricionistaRepository
                        .buscarPorUsuarioId(usuarioId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Nutricionista não encontrado"
                                )
                        );

                autorizado = Objects.equals(
                        agendamento.getNutricionistaId(),
                        nutricionista.getId()
                );
            }

            default -> throw new RuntimeException(
                    "Usuário não possui permissão para excluir agendamentos"
            );
        }

        if (!autorizado) {
            throw new RuntimeException(
                    "Você não tem permissão para excluir este agendamento"
            );
        }

        agendamentoRepository.excluir(agendamentoId);
    }
}