package com.neogym.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neogym.application.dto.request.CadastrarAlunoRequest;
import com.neogym.application.dto.request.CadastrarNutricionistaRequest;
import com.neogym.application.dto.request.CadastrarPersonalRequest;
import com.neogym.application.dto.request.LoginRequest;
import com.neogym.application.dto.request.RefreshTokenRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("AuthController - Integração")
class AuthControllerIntegrationTest {

    @Autowired MockMvc      mockMvc;
    @Autowired ObjectMapper objectMapper;

    // shared across ordered tests
    private static String refreshTokenGlobal;

    // ── Cadastros ─────────────────────────────────────────────────────────────

    @Test @Order(1)
    @DisplayName("POST /cadastro/aluno deve retornar 201")
    void deveCadastrarAluno() throws Exception {
        mockMvc.perform(post("/api/v1/auth/cadastro/aluno")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                CadastrarAlunoRequest.builder()
                                        .nome("João Integração")
                                        .email("joao@neogym.com")
                                        .senha("Senha123")
                                        .metaAguaMl(2000)
                                        .build())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("joao@neogym.com"))
                .andExpect(jsonPath("$.ativo").value(true))
                .andExpect(jsonPath("$.metaAguaMl").value(2000));
    }

    @Test @Order(2)
    @DisplayName("POST /cadastro/personal deve retornar 201 com statusCref PENDENTE")
    void deveCadastrarPersonal() throws Exception {
        mockMvc.perform(post("/api/v1/auth/cadastro/personal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                CadastrarPersonalRequest.builder()
                                        .nome("Carlos Personal")
                                        .email("carlos@neogym.com")
                                        .senha("Senha123")
                                        .cref("012345-G")
                                        .estadoCref("SP")
                                        .build())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCref").value("PENDENTE"))
                .andExpect(jsonPath("$.cref").value("012345-G"));
    }

    @Test @Order(3)
    @DisplayName("POST /cadastro/nutricionista deve retornar 201 com statusCrn PENDENTE")
    void deveCadastrarNutricionista() throws Exception {
        mockMvc.perform(post("/api/v1/auth/cadastro/nutricionista")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                CadastrarNutricionistaRequest.builder()
                                        .nome("Ana Nutri")
                                        .email("ana@neogym.com")
                                        .senha("Senha123")
                                        .crn("12345")
                                        .estadoCrn("SP")
                                        .build())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCrn").value("PENDENTE"));
    }

    @Test @Order(4)
    @DisplayName("POST /cadastro/aluno deve retornar 409 com email duplicado")
    void deveRetornar409EmailDuplicado() throws Exception {
        mockMvc.perform(post("/api/v1/auth/cadastro/aluno")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                CadastrarAlunoRequest.builder()
                                        .nome("Dup").email("joao@neogym.com").senha("Senha123").build())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test @Order(5)
    @DisplayName("POST /cadastro/aluno deve retornar 400 com campos inválidos")
    void deveRetornar400CamposInvalidos() throws Exception {
        mockMvc.perform(post("/api/v1/auth/cadastro/aluno")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                CadastrarAlunoRequest.builder()
                                        .nome("").email("nao-e-email").senha("123").build())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos", hasSize(greaterThan(0))));
    }

    // ── Login ─────────────────────────────────────────────────────────────────

    @Test @Order(6)
    @DisplayName("POST /login deve retornar 200 com access e refresh token")
    void deveRealizarLogin() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                LoginRequest.builder()
                                        .email("joao@neogym.com").senha("Senha123").build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.tipo").value("ALUNO"))
                .andReturn();

        refreshTokenGlobal = objectMapper.readTree(
                result.getResponse().getContentAsString()).get("refreshToken").asText();
    }

    @Test @Order(7)
    @DisplayName("POST /login deve retornar 401 com senha errada")
    void deveRetornar401SenhaErrada() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                LoginRequest.builder()
                                        .email("joao@neogym.com").senha("senhaErrada99").build())))
                .andExpect(status().isUnauthorized());
    }

    // ── Refresh ───────────────────────────────────────────────────────────────

    @Test @Order(8)
    @DisplayName("POST /refresh deve rotacionar e emitir novos tokens diferentes")
    void deveRotacionarRefreshToken() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                RefreshTokenRequest.builder()
                                        .refreshToken(refreshTokenGlobal).build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").value(not(equalTo(refreshTokenGlobal))))
                .andReturn();

        refreshTokenGlobal = objectMapper.readTree(
                result.getResponse().getContentAsString()).get("refreshToken").asText();
    }

    @Test @Order(9)
    @DisplayName("POST /refresh deve retornar 401 ao reusar token rotacionado (replay attack)")
    void deveBloquearReplayAttack() throws Exception {
        String tokenParaReusar = refreshTokenGlobal;

        // Primeiro uso - válido
        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                RefreshTokenRequest.builder().refreshToken(tokenParaReusar).build())))
                .andExpect(status().isOk());

        // Segundo uso do mesmo token - deve ser bloqueado (já revogado pela rotation)
        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                RefreshTokenRequest.builder().refreshToken(tokenParaReusar).build())))
                .andExpect(status().isUnauthorized());
    }

    // ── Logout ────────────────────────────────────────────────────────────────

    @Test @Order(10)
    @DisplayName("POST /logout deve retornar 204 e revogar token")
    void deveRealizarLogout() throws Exception {
        // Faz novo login para pegar token fresco
        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                LoginRequest.builder()
                                        .email("joao@neogym.com").senha("Senha123").build())))
                .andExpect(status().isOk()).andReturn();

        String token = objectMapper.readTree(
                loginResult.getResponse().getContentAsString()).get("refreshToken").asText();

        // Logout
        mockMvc.perform(post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                RefreshTokenRequest.builder().refreshToken(token).build())))
                .andExpect(status().isNoContent());

        // Tenta usar o token revogado - deve falhar
        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                RefreshTokenRequest.builder().refreshToken(token).build())))
                .andExpect(status().isUnauthorized());
    }
}
