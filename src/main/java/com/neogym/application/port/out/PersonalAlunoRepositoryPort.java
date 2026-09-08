package com.neogym.application.port.out;

public interface PersonalAlunoRepositoryPort {

    boolean existeVinculo(Long personalId, Long alunoId);
}