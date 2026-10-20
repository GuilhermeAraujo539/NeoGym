package com.neogym.application.port.out;
import java.util.List;
import com.neogym.domain.entity.FichaTreino;

public interface FichaTreinoRepositoryPort {

    FichaTreino salvar(FichaTreino fichaTreino);

    List<FichaTreino> buscarPorAlunoId(Long alunoId);
}