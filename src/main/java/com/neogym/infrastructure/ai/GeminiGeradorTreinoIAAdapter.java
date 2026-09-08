package com.neogym.infrastructure.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.gaos.models.interactions.CreateModelInteraction;
import com.google.genai.gaos.models.interactions.CreateModelInteractionResponseFormat;
import com.google.genai.gaos.models.interactions.InteractionsInput;
import com.google.genai.gaos.models.interactions.Model;
import com.google.genai.gaos.models.interactions.ResponseFormat;
import com.google.genai.gaos.models.interactions.TextResponseFormat;
import com.google.genai.gaos.models.operations.CreateInteractionRequestBody;
import com.neogym.application.dto.request.GerarTreinoIARequest;
import com.neogym.application.dto.response.ExercicioIAResponse;
import com.google.genai.gaos.models.interactions.TextResponseFormatMimeType;
import com.neogym.application.dto.response.TreinoIAResponse;
import com.neogym.application.port.out.GeradorTreinoIAPort;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GeminiGeradorTreinoIAAdapter implements GeradorTreinoIAPort {

    private final Client client;
    private final ObjectMapper objectMapper;

    public GeminiGeradorTreinoIAAdapter(
            Client client,
            ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
    }

    @Override
    public TreinoIAResponse gerar(GerarTreinoIARequest request) {

        String prompt = montarPrompt(request);

        CreateModelInteraction params =
                CreateModelInteraction.builder()
                        .model(Model.of("gemini-3.8-flash"))
                        .input(InteractionsInput.of(prompt))
                        .build();

        var interaction = client.interactions
                .create(CreateInteractionRequestBody.of(params))
                .interaction()
                .get();

        System.out.println("========== GEMINI ==========");
        System.out.println("Output text: " + interaction.outputText());
        System.out.println("Interaction: " + interaction);
        System.out.println("============================");

        String resposta = interaction
                .outputText()
                .orElseThrow(() ->
                        new RuntimeException(
                                "Gemini não retornou uma resposta"
                        )
                );

        try {
            return objectMapper.readValue(
                    resposta,
                    TreinoIAResponse.class
            );
        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao interpretar resposta do Gemini: " + resposta,
                    e
            );
        }
    }

    private String montarPrompt(GerarTreinoIARequest request) {

        return """
                Você é um personal trainer profissional responsável por
                criar sugestões de treinos para alunos de academia.

                Gere um treino de musculação baseado nas informações abaixo.

                Objetivo:
                %s

                Nível do aluno:
                %s

                Dias por semana:
                %d

                Duração aproximada do treino:
                %d minutos

                Observações:
                %s

                Regras:

                - Gere exercícios adequados ao objetivo e nível informado.
                - Respeite a duração aproximada informada.
                - Distribua os exercícios de maneira coerente.
                - Informe séries e repetições.
                - Não invente informações sobre o aluno que não foram fornecidas.
                - Retorne somente o JSON solicitado.
                - Não inclua explicações fora do JSON.

                O treino será revisado por um profissional antes de ser salvo.
                """.formatted(
                request.getObjetivo(),
                request.getNivel(),
                request.getDiasSemana(),
                request.getDuracaoMinutos(),
                request.getObservacoes() == null
                        ? "Nenhuma"
                        : request.getObservacoes()
        );
    }

    private Map<String, Object> criarSchema() {

        Map<String, Object> exercicio = new HashMap<>();

        exercicio.put("type", "object");

        exercicio.put(
                "properties",
                Map.of(
                        "nome", Map.of("type", "string"),
                        "series", Map.of("type", "integer"),
                        "repeticoes", Map.of("type", "integer")
                )
        );

        exercicio.put(
                "required",
                List.of(
                        "nome",
                        "series",
                        "repeticoes"
                )
        );

        Map<String, Object> schema = new HashMap<>();

        schema.put("type", "object");

        schema.put(
                "properties",
                Map.of(
                        "nome", Map.of("type", "string"),
                        "objetivo", Map.of("type", "string"),
                        "duracao", Map.of("type", "integer"),
                        "exercicios", Map.of(
                                "type", "array",
                                "items", exercicio
                        )
                )
        );

        schema.put(
                "required",
                List.of(
                        "nome",
                        "objetivo",
                        "duracao",
                        "exercicios"
                )
        );

        return schema;
    }
}