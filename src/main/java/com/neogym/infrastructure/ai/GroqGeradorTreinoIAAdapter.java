package com.neogym.infrastructure.ai;

import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.StructuredChatCompletion;
import com.openai.models.chat.completions.StructuredChatCompletionCreateParams;
import com.neogym.application.dto.request.GerarTreinoIARequest;
import com.neogym.application.dto.response.TreinoIAResponse;
import com.neogym.application.port.out.GeradorTreinoIAPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GroqGeradorTreinoIAAdapter implements GeradorTreinoIAPort {

    private final OpenAIClient client;
    private final String model;

    public GroqGeradorTreinoIAAdapter(
            OpenAIClient client,
            @Value("${groq.model}") String model) {

        this.client = client;
        this.model = model;
    }

    @Override
    public TreinoIAResponse gerar(GerarTreinoIARequest request) {

        String prompt = montarPrompt(request);

        StructuredChatCompletionCreateParams<TreinoIAResponse> params =
                ChatCompletionCreateParams.builder()
                        .model(ChatModel.of(model))
                        .addUserMessage(prompt)
                        .responseFormat(TreinoIAResponse.class)
                        .build();

        StructuredChatCompletion<TreinoIAResponse> response =
                client.chat()
                        .completions()
                        .create(params);

        return response.choices()
                .get(0)
                .message()
                .content()
                .orElseThrow(() ->
                        new RuntimeException(
                                "Groq não retornou uma resposta estruturada"
                        )
                );
    }

    private String montarPrompt(GerarTreinoIARequest request) {

        return """
                Você é um personal trainer profissional.

                Gere uma sugestão de treino de musculação para o aluno.

                Regras:
                - Gere exercícios adequados ao objetivo e nível informado.
                - Respeite a duração aproximada informada.
                - Distribua os exercícios de forma coerente.
                - Informe séries e repetições para cada exercício.
                - Não invente informações sobre o aluno que não foram fornecidas.
                - Retorne somente os dados necessários para o treino.
                
                Objetivo:
                %s

                Nível:
                %s

                Dias por semana:
                %d

                Duração:
                %d minutos

                Observações:
                %s
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
}