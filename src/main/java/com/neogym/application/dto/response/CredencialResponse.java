package com.neogym.application.dto.response;

import com.neogym.domain.enums.StatusCredencial;
import com.neogym.domain.enums.TipoCredencial;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CredencialResponse {
    private Long             id;
    private Long             usuarioId;
    private String           nomeUsuario;
    private String           emailUsuario;
    private TipoCredencial   tipo;
    private String           arquivoUrl;
    private StatusCredencial status;
    private String           observacaoAdmin;
    private LocalDateTime    criadoEm;
    private LocalDateTime    avaliadoEm;
    private Long             avaliadoPorId;
    private String           nomeAvaliador;
}
