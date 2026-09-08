package com.neogym.application.usecase;

import com.neogym.application.dto.request.GerarTreinoIARequest;
import com.neogym.application.dto.response.TreinoIAResponse;
import com.neogym.application.port.in.GerarTreinoIAUseCase;
import com.neogym.application.port.out.AlunoRepositoryPort;
import com.neogym.application.port.out.GeradorTreinoIAPort;
import com.neogym.application.port.out.PersonalAlunoRepositoryPort;
import com.neogym.application.port.out.PersonalRepositoryPort;

public class GerarTreinoIAService implements GerarTreinoIAUseCase {

    private final GeradorTreinoIAPort geradorTreinoIAPort;
    private final PersonalRepositoryPort personalRepository;
    private final AlunoRepositoryPort alunoRepository;
    private final PersonalAlunoRepositoryPort personalAlunoRepository;

    public GerarTreinoIAService(
            GeradorTreinoIAPort geradorTreinoIAPort,
            PersonalRepositoryPort personalRepository,
            AlunoRepositoryPort alunoRepository,
            PersonalAlunoRepositoryPort personalAlunoRepository) {

        this.geradorTreinoIAPort = geradorTreinoIAPort;
        this.personalRepository = personalRepository;
        this.alunoRepository = alunoRepository;
        this.personalAlunoRepository = personalAlunoRepository;
    }

    @Override
    public TreinoIAResponse executar(
            Long usuarioId,
            GerarTreinoIARequest request) {

        var personal = personalRepository
                .buscarPorUsuarioId(usuarioId)
                .orElseThrow(() ->
                        new RuntimeException("Personal não encontrado"));

        if (!personal.isAprovado()) {
            throw new RuntimeException(
                    "Personal não está aprovado");
        }

        alunoRepository
                .buscarPorId(request.getAlunoId())
                .orElseThrow(() ->
                        new RuntimeException("Aluno não encontrado"));

        boolean possuiVinculo =
                personalAlunoRepository.existeVinculo(
                        personal.getId(),
                        request.getAlunoId());

        if (!possuiVinculo) {
            throw new RuntimeException(
                    "Personal não possui vínculo com este aluno");
        }

        return geradorTreinoIAPort.gerar(request);
    }
}