package com.neogym.application.usecase;

import com.neogym.application.dto.response.AgendamentoResponse;
import com.neogym.application.port.in.ListarMeusAgendamentosUseCase;
import com.neogym.application.port.out.AgendamentoRepositoryPort;
import com.neogym.application.port.out.AlunoRepositoryPort;
import com.neogym.application.port.out.NutricionistaRepositoryPort;
import com.neogym.application.port.out.PersonalRepositoryPort;
import com.neogym.application.port.out.UsuarioRepositoryPort;
import com.neogym.domain.entity.Agendamento;
import com.neogym.domain.enums.TipoProfissionalAgendamento;

import java.util.List;

public class ListarMeusAgendamentosService
        implements ListarMeusAgendamentosUseCase {

    private final AgendamentoRepositoryPort agendamentoRepository;
    private final AlunoRepositoryPort alunoRepository;
    private final PersonalRepositoryPort personalRepository;
    private final NutricionistaRepositoryPort nutricionistaRepository;
    private final UsuarioRepositoryPort usuarioRepository;

    public ListarMeusAgendamentosService(
            AgendamentoRepositoryPort agendamentoRepository,
            AlunoRepositoryPort alunoRepository,
            PersonalRepositoryPort personalRepository,
            NutricionistaRepositoryPort nutricionistaRepository,
            UsuarioRepositoryPort usuarioRepository
    ) {
        this.agendamentoRepository = agendamentoRepository;
        this.alunoRepository = alunoRepository;
        this.personalRepository = personalRepository;
        this.nutricionistaRepository = nutricionistaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<AgendamentoResponse> executar(
            Long usuarioId,
            String tipoUsuario
    ) {

        List<Agendamento> agendamentos;

        if ("ROLE_ALUNO".equals(tipoUsuario)) {

            var aluno = alunoRepository
                    .buscarPorUsuarioId(usuarioId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Aluno não encontrado"
                            )
                    );

            agendamentos =
                    agendamentoRepository
                            .buscarPorAlunoId(
                                    aluno.getId()
                            );

        } else if ("ROLE_PERSONAL".equals(tipoUsuario)) {

            var personal = personalRepository
                    .buscarPorUsuarioId(usuarioId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Personal não encontrado"
                            )
                    );

            agendamentos =
                    agendamentoRepository
                            .buscarPorPersonalId(
                                    personal.getId()
                            );

        } else if ("ROLE_NUTRICIONISTA".equals(tipoUsuario)) {

            var nutricionista =
                    nutricionistaRepository
                            .buscarPorUsuarioId(usuarioId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Nutricionista não encontrado"
                                    )
                            );

            agendamentos =
                    agendamentoRepository
                            .buscarPorNutricionistaId(
                                    nutricionista.getId()
                            );

        } else {
            throw new RuntimeException(
                    "Usuário não possui agenda"
            );
        }

        return agendamentos
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private AgendamentoResponse toResponse(
            Agendamento agendamento
    ) {

        Long profissionalId;
        String profissionalNome;

        if (agendamento.getTipoProfissional()
                == TipoProfissionalAgendamento.PERSONAL) {

            profissionalId = agendamento.getPersonalId();

            var personal = personalRepository
                    .buscarPorId(profissionalId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Personal não encontrado"
                            )
                    );

            profissionalNome = usuarioRepository
                    .buscarPorId(personal.getUsuarioId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Usuário do Personal não encontrado"
                            )
                    )
                    .getNome();

        } else {

            profissionalId = agendamento.getNutricionistaId();

            var nutricionista = nutricionistaRepository
                    .buscarPorId(profissionalId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Nutricionista não encontrado"
                            )
                    );

            profissionalNome = usuarioRepository
                    .buscarPorId(nutricionista.getUsuarioId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Usuário da Nutricionista não encontrado"
                            )
                    )
                    .getNome();
        }

        return AgendamentoResponse.builder()
                .id(agendamento.getId())
                .alunoId(agendamento.getAlunoId())
                .profissionalId(profissionalId)
                .profissionalNome(profissionalNome)
                .tipoProfissional(
                        agendamento.getTipoProfissional()
                )
                .titulo(agendamento.getTitulo())
                .descricao(agendamento.getDescricao())
                .dataHora(agendamento.getDataHora())
                .status(agendamento.getStatus())
                .criadoEm(agendamento.getCriadoEm())
                .build();
    }
}