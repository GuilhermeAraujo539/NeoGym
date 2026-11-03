package com.neogym.application.usecase;

import com.neogym.application.dto.response.FotoGaleriaProfissionalConteudoResponse;
import com.neogym.application.dto.response.FotoGaleriaProfissionalResponse;
import com.neogym.application.port.in.GerenciarGaleriaProfissionalUseCase;
import com.neogym.application.port.out.NutricionistaRepositoryPort;
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
    private final NutricionistaRepositoryPort nutricionistaRepository;

    public GerenciarGaleriaProfissionalService(
            PersonalRepositoryPort personalRepository,
            PersonalFotoRepositoryPort fotoRepository,
            NutricionistaRepositoryPort nutricionistaRepository) {

        this.personalRepository = personalRepository;
        this.fotoRepository = fotoRepository;
        this.nutricionistaRepository = nutricionistaRepository;
    }

    @Override
    public List<FotoGaleriaProfissionalResponse> listar(Long usuarioId) {
        validarPerfilProfissional(usuarioId);

        return fotoRepository.listarPorUsuarioId(usuarioId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public FotoGaleriaProfissionalResponse adicionar(
            Long usuarioId,
            byte[] dados,
            String contentType) {

        validarPerfilProfissional(usuarioId);
        validarImagem(dados, contentType);

        long quantidade = fotoRepository.contarPorUsuarioId(usuarioId);

        if (quantidade >= MAX_FOTOS) {
            throw new IllegalStateException(
                    "O limite de 8 fotos foi atingido."
            );
        }

        String tipo = normalizarContentType(contentType);

        var foto = new FotoGaleriaProfissional(
                null,
                usuarioId,
                dados,
                tipo,
                LocalDateTime.now()
        );

        return toResponse(fotoRepository.salvar(foto));
    }

    @Override
    public FotoGaleriaProfissionalConteudoResponse obterConteudo(
            Long usuarioId,
            Long fotoId) {

        validarPerfilProfissional(usuarioId);

        var foto = fotoRepository
                .buscarPorIdEUsuarioId(fotoId, usuarioId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Foto não encontrada ou sem permissão."
                        )
                );

        return new FotoGaleriaProfissionalConteudoResponse(
                foto.getDados(),
                foto.getContentType()
        );
    }

    @Override
    public void excluir(Long usuarioId, Long fotoId) {
        validarPerfilProfissional(usuarioId);

        fotoRepository
                .buscarPorIdEUsuarioId(fotoId, usuarioId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Foto não encontrada ou sem permissão."
                        )
                );

        fotoRepository.excluir(fotoId, usuarioId);
    }

    private void validarPerfilProfissional(Long usuarioId) {
        if (usuarioId == null || usuarioId <= 0) {
            throw new IllegalArgumentException(
                    "Usuário inválido."
            );
        }

        boolean ehPersonal = personalRepository
                .buscarPorUsuarioId(usuarioId)
                .isPresent();

        boolean ehNutricionista = nutricionistaRepository
                .buscarPorUsuarioId(usuarioId)
                .isPresent();

        if (!ehPersonal && !ehNutricionista) {
            throw new IllegalArgumentException(
                    "Perfil profissional não encontrado."
            );
        }
    }

    private void validarImagem(byte[] dados, String contentType) {
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

        String tipo = normalizarContentType(contentType);

        if (!TIPOS_PERMITIDOS.contains(tipo)) {
            throw new IllegalArgumentException(
                    "Formato não permitido. Utilize JPEG, PNG ou WebP."
            );
        }
    }

    private String normalizarContentType(String contentType) {
        return contentType == null
                ? ""
                : contentType.toLowerCase(Locale.ROOT).trim();
    }

    private FotoGaleriaProfissionalResponse toResponse(
            FotoGaleriaProfissional foto) {

        return FotoGaleriaProfissionalResponse.builder()
                .id(foto.getId())
                .contentType(foto.getContentType())
                .criadoEm(foto.getCriadoEm())
                .build();
    }
}