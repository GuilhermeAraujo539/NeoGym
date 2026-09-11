package com.neogym.domain.entity;

import com.neogym.domain.enums.StatusVinculo;
import com.neogym.domain.enums.TipoUsuario;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class VinculoProfissional {

    private Long id;
    private Long alunoId;
    private Long profissionalUsuarioId;
    private TipoUsuario tipoProfissional;
    private StatusVinculo status;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public void alterarStatus(StatusVinculo status) {
        this.status = status;
        this.atualizadoEm = LocalDateTime.now();
    }
}