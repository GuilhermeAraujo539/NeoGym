package com.neogym.application.usecase;

import com.neogym.application.dto.response.FotoGaleriaProfissionalConteudoResponse;
import com.neogym.application.dto.response.FotoGaleriaProfissionalResponse;
import com.neogym.application.port.in.GerenciarGaleriaProfissionalUseCase;
import com.neogym.application.port.out.PersonalFotoRepositoryPort;
import com.neogym.application.port.out.PersonalRepositoryPort;
import com.neogym.domain.entity.FotoGaleriaProfissional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

public class GerenciarGaleriaProfissionalService
        implements GerenciarGaleriaProfissionalUseCase {

    private static final int MAX_FOTOS = 8;
    private static final int MAX_BYTES = 5 * 1024 * 1024;

    private static final List<String> TIPOS_PERMITIDOS = List.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private final PersonalRepositoryPort personalRepository;
    private final PersonalFotoRepositoryPort fotoRepository;

    public GerenciarGaleriaProfissionalService(
            PersonalRepositoryPort personalRepository,
            PersonalFotoRepositoryPort fotoRepository
    ) {
        this.personalRepository = personalRepository;
        this.fotoRepository = fotoRepository;
    }

    @Override
    public List<FotoGaleriaProfissionalResponse> listar(Long usuarioId) {
        var personal = buscarPersonal(usuarioId);

        return fotoRepository
                .listarPorPersonalId(personal.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public FotoGaleriaProfissionalResponse adicionar(
            Long usuarioId,
            byte[] dados,
            String contentType
    ) {
        var personal = buscarPersonal(usuarioId);

        if (dados == null || dados.length == 0) {
            throw new IllegalArgumentException(
                    "Selecione uma imagem válida."
            );
        }

        if (dados.length > MAX_BYTES) {
            throw new IllegalArgumentException(
                    "Cada foto deve ter no máximo 5 MB."
            );
        }

        String tipo = contentType == null
                ? ""
                : contentType.toLowerCase(Locale.ROOT).trim();

        if (!TIPOS_PERMITIDOS.contains(tipo)) {
            throw new IllegalArgumentException(
                    "Formato não permitido. Utilize JPEG, PNG ou WebP."
            );
        }

        if (fotoRepository.contarPorPersonalId(personal.getId())
                >= MAX_FOTOS) {
            throw new IllegalStateException(
                    "O limite de 8 fotos foi atingido."
            );
        }

        var foto = new FotoGaleriaProfissional(
                null,
                personal.getId(),
                dados,
                tipo,
                LocalDateTime.now()
        );

        return toResponse(fotoRepository.salvar(foto));
    }

    @Override
    public FotoGaleriaProfissionalConteudoResponse obterConteudo(
            Long usuarioId,
            Long fotoId
    ) {
        var personal = buscarPersonal(usuarioId);

        var foto = fotoRepository
                .buscarPorIdEPersonalId(fotoId, personal.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Foto não encontrada."
                        )
                );

        return new FotoGaleriaProfissionalConteudoResponse(
                foto.getDados(),
                foto.getContentType()
        );
    }

    @Override
    public void excluir(Long usuarioId, Long fotoId) {
        var personal = buscarPersonal(usuarioId);

        // A consulta confirma que a foto pertence ao personal autenticado.
        fotoRepository
                .buscarPorIdEPersonalId(fotoId, personal.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Foto não encontrada ou sem permissão."
                        )
                );

        fotoRepository.excluir(fotoId);
    }

    private com.neogym.domain.entity.Personal buscarPersonal(
            Long usuarioId
    ) {
        return personalRepository
                .buscarPorUsuarioId(usuarioId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Perfil profissional não encontrado."
                        )
                );
    }

    private FotoGaleriaProfissionalResponse toResponse(
            FotoGaleriaProfissional foto
    ) {
        return FotoGaleriaProfissionalResponse.builder()
                .id(foto.getId())
                .contentType(foto.getContentType())
                .criadoEm(foto.getCriadoEm())
                .build();
    }
}