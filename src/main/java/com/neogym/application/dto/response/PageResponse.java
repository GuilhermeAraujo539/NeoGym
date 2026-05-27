package com.neogym.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PageResponse<T> {
    private List<T> conteudo;
    private int     pagina;
    private int     tamanhoPagina;
    private long    totalElementos;
    private int     totalPaginas;
    private boolean primeira;
    private boolean ultima;
}
