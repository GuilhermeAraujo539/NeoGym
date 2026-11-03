package com.neogym.application.usecase;

import com.neogym.application.port.in.ConcluirAgendamentoUseCase;
import com.neogym.application.port.out.AgendamentoRepositoryPort;
import com.neogym.application.port.out.NutricionistaRepositoryPort;
import com.neogym.application.port.out.PersonalRepositoryPort;
import com.neogym.domain.entity.Agendamento;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ConcluirAgendamentoService
        implements ConcluirAgendamentoUseCase {

    private final AgendamentoRepositoryPort agendamentoRepository;
    private final PersonalRepositoryPort personalRepository;
    private final NutricionistaRepositoryPort nutricionistaRepository;

    public ConcluirAgendamentoService(
            AgendamentoRepositoryPort agendamentoRepository,
            PersonalRepositoryPort personalRepository,
            NutricionistaRepositoryPort nutricionistaRepository
    ) {
        this.agendamentoRepository = agendamentoRepository;
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
                        new RuntimeException("Agendamento não encontrado")
                );

        boolean autorizado;

        switch (tipoUsuario) {
            case "ROLE_PERSONAL" -> {
                var personal = personalRepository
                        .buscarPorUsuarioId(usuarioId)
                        .orElseThrow(() ->
                                new RuntimeException("Personal não encontrado")
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
                    "Usuário não possui permissão para concluir agendamentos"
            );
        }

        if (!autorizado) {
            throw new RuntimeException(
                    "Você não tem permissão para concluir este agendamento"
            );
        }

        agendamento.concluir();

        agendamentoRepository.salvar(agendamento);
    }
}