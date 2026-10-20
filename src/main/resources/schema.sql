-- ============================================================
-- NeoGym - Schema completo
-- PostgreSQL 18 + PostGIS
-- ============================================================

-- ============================================================
-- EXTENSÕES
-- ============================================================

CREATE EXTENSION IF NOT EXISTS postgis;


-- ============================================================
-- TIPOS
-- ============================================================

CREATE TYPE tipo_usuario AS ENUM (
    'ALUNO',
    'PERSONAL',
    'NUTRICIONISTA',
    'ACADEMIA',
    'ADMIN'
);

CREATE TYPE status_credencial AS ENUM (
    'PENDENTE',
    'APROVADO',
    'REJEITADO'
);

CREATE TYPE status_vinculo AS ENUM (
    'PENDENTE',
    'APROVADO',
    'REJEITADO'
);

CREATE TYPE status_sessao AS ENUM (
    'AGENDADA',
    'CONFIRMADA',
    'CANCELADA',
    'CONCLUIDA'
);

CREATE TYPE tipo_sessao AS ENUM (
    'PERSONAL',
    'NUTRICIONAL'
);

CREATE TYPE tipo_chat AS ENUM (
    'PRIVADO',
    'GRUPO'
);

CREATE TYPE tipo_token AS ENUM (
    'REFRESH',
    'RESET_SENHA'
);


-- ============================================================
-- USUARIO
-- ============================================================

CREATE TABLE usuario (
                         id BIGSERIAL PRIMARY KEY,

                         nome VARCHAR(120) NOT NULL,

                         email VARCHAR(120) NOT NULL UNIQUE,

                         senha_hash VARCHAR(255) NOT NULL,

                         tipo tipo_usuario NOT NULL,

                         ativo BOOLEAN NOT NULL DEFAULT TRUE,

                         criado_em TIMESTAMP NOT NULL DEFAULT NOW()
);


-- ============================================================
-- ACADEMIA
-- ============================================================

CREATE TABLE academia (
                          id BIGSERIAL PRIMARY KEY,

                          nome VARCHAR(120) NOT NULL,

                          cnpj VARCHAR(18) NOT NULL UNIQUE,

    -- PostGIS
    -- SRID 4326 = latitude/longitude GPS
                          localizacao GEOGRAPHY(POINT, 4326),

                          ativo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Índice espacial
CREATE INDEX idx_academia_localizacao
    ON academia
    USING GIST (localizacao);


-- ============================================================
-- ALUNO
-- ============================================================

CREATE TABLE aluno (
                       id BIGSERIAL PRIMARY KEY,

                       usuario_id BIGINT NOT NULL UNIQUE,

                       academia_id BIGINT,

                       peso NUMERIC(5,2),

                       altura NUMERIC(4,2),

                       meta_agua_ml INTEGER DEFAULT 2000,

                       CONSTRAINT fk_aluno_usuario
                           FOREIGN KEY (usuario_id)
                               REFERENCES usuario(id)
                               ON DELETE CASCADE,

                       CONSTRAINT fk_aluno_academia
                           FOREIGN KEY (academia_id)
                               REFERENCES academia(id)
                               ON DELETE SET NULL
);


-- ============================================================
-- PERSONAL
-- ============================================================

CREATE TABLE personal (
                          id BIGSERIAL PRIMARY KEY,

                          usuario_id BIGINT NOT NULL UNIQUE,

                          cref VARCHAR(20) NOT NULL,

                          estado_cref CHAR(2) NOT NULL,

                          status_cref status_credencial NOT NULL DEFAULT 'PENDENTE',

                          CONSTRAINT fk_personal_usuario
                              FOREIGN KEY (usuario_id)
                                  REFERENCES usuario(id)
                                  ON DELETE CASCADE
);


-- ============================================================
-- NUTRICIONISTA
-- ============================================================

CREATE TABLE nutricionista (
                               id BIGSERIAL PRIMARY KEY,

                               usuario_id BIGINT NOT NULL UNIQUE,

                               crn VARCHAR(20) NOT NULL,

                               estado_crn CHAR(2) NOT NULL,

                               status_crn status_credencial NOT NULL DEFAULT 'PENDENTE',

                               CONSTRAINT fk_nutricionista_usuario
                                   FOREIGN KEY (usuario_id)
                                       REFERENCES usuario(id)
                                       ON DELETE CASCADE
);


-- ============================================================
-- CREDENCIAL
-- ============================================================

CREATE TABLE credencial (
                            id BIGSERIAL PRIMARY KEY,

                            usuario_id BIGINT NOT NULL,

                            tipo VARCHAR(10) NOT NULL,

                            arquivo_url VARCHAR(255),

                            status status_credencial NOT NULL DEFAULT 'PENDENTE',

                            CONSTRAINT fk_credencial_usuario
                                FOREIGN KEY (usuario_id)
                                    REFERENCES usuario(id)
                                    ON DELETE CASCADE
);


-- ============================================================
-- PERSONAL x ACADEMIA
-- ============================================================

CREATE TABLE personal_academia (
                                   id BIGSERIAL PRIMARY KEY,

                                   personal_id BIGINT NOT NULL,

                                   academia_id BIGINT NOT NULL,

                                   status status_vinculo NOT NULL DEFAULT 'PENDENTE',

                                   data_inicio DATE,

                                   CONSTRAINT fk_personal_academia_personal
                                       FOREIGN KEY (personal_id)
                                           REFERENCES personal(id)
                                           ON DELETE CASCADE,

                                   CONSTRAINT fk_personal_academia_academia
                                       FOREIGN KEY (academia_id)
                                           REFERENCES academia(id)
                                           ON DELETE CASCADE,

                                   CONSTRAINT uk_personal_academia
                                       UNIQUE (personal_id, academia_id)
);


-- ============================================================
-- REFRESH TOKEN
-- ============================================================

CREATE TABLE refresh_token (
                               id BIGSERIAL PRIMARY KEY,

                               usuario_id BIGINT NOT NULL,

                               token_hash VARCHAR(255) NOT NULL UNIQUE,

                               expira_em TIMESTAMP NOT NULL,

                               revogado BOOLEAN NOT NULL DEFAULT FALSE,

                               criado_em TIMESTAMP NOT NULL DEFAULT NOW(),

                               ip_origem VARCHAR(45),

                               user_agent VARCHAR(255),

                               CONSTRAINT fk_refresh_token_usuario
                                   FOREIGN KEY (usuario_id)
                                       REFERENCES usuario(id)
                                       ON DELETE CASCADE
);


-- ============================================================
-- FICHA DE TREINO
-- ============================================================

CREATE TABLE personal_aluno (
                              id BIGSERIAL PRIMARY KEY,

                              aluno_id BIGINT NOT NULL,

                              personal_id BIGINT NOT NULL,

                              descricao TEXT NOT NULL,

                              criada_em TIMESTAMP NOT NULL DEFAULT NOW(),

                              CONSTRAINT fk_ficha_aluno
                                  FOREIGN KEY (aluno_id)
                                      REFERENCES aluno(id)
                                      ON DELETE CASCADE,

                              CONSTRAINT fk_ficha_personal
                                  FOREIGN KEY (personal_id)
                                      REFERENCES personal(id)
                                      ON DELETE RESTRICT
);


-- ============================================================
-- REGISTRO DE ÁGUA
-- ============================================================

CREATE TABLE registro_agua (
                               id BIGSERIAL PRIMARY KEY,

                               aluno_id BIGINT NOT NULL,

                               data DATE NOT NULL,

                               quantidade_ml INTEGER NOT NULL,

                               CONSTRAINT fk_registro_agua_aluno
                                   FOREIGN KEY (aluno_id)
                                       REFERENCES aluno(id)
                                       ON DELETE CASCADE,

                               CONSTRAINT uk_registro_agua_aluno_data
                                   UNIQUE (aluno_id, data)
);


-- ============================================================
-- SESSÃO
-- ============================================================

CREATE TABLE sessao (
                        id BIGSERIAL PRIMARY KEY,

                        aluno_id BIGINT NOT NULL,

                        profissional_id BIGINT NOT NULL,

                        academia_id BIGINT,

                        tipo tipo_sessao NOT NULL,

                        data_hora TIMESTAMP NOT NULL,

                        status status_sessao NOT NULL DEFAULT 'AGENDADA',

                        CONSTRAINT fk_sessao_aluno
                            FOREIGN KEY (aluno_id)
                                REFERENCES aluno(id)
                                ON DELETE RESTRICT,

                        CONSTRAINT fk_sessao_profissional
                            FOREIGN KEY (profissional_id)
                                REFERENCES usuario(id)
                                ON DELETE RESTRICT,

                        CONSTRAINT fk_sessao_academia
                            FOREIGN KEY (academia_id)
                                REFERENCES academia(id)
                                ON DELETE SET NULL
);


-- ============================================================
-- CHAT
-- ============================================================

CREATE TABLE chat (
                      id BIGSERIAL PRIMARY KEY,

                      tipo tipo_chat NOT NULL DEFAULT 'PRIVADO',

                      criado_em TIMESTAMP NOT NULL DEFAULT NOW()
);


-- ============================================================
-- CHAT PARTICIPANTE
-- ============================================================

CREATE TABLE chat_participante (
                                   id BIGSERIAL PRIMARY KEY,

                                   chat_id BIGINT NOT NULL,

                                   usuario_id BIGINT NOT NULL,

                                   CONSTRAINT fk_chat_participante_chat
                                       FOREIGN KEY (chat_id)
                                           REFERENCES chat(id)
                                           ON DELETE CASCADE,

                                   CONSTRAINT fk_chat_participante_usuario
                                       FOREIGN KEY (usuario_id)
                                           REFERENCES usuario(id)
                                           ON DELETE CASCADE,

                                   CONSTRAINT uk_chat_participante
                                       UNIQUE (chat_id, usuario_id)
);


-- ============================================================
-- MENSAGEM
-- ============================================================

CREATE TABLE mensagem (
                          id BIGSERIAL PRIMARY KEY,

                          chat_id BIGINT NOT NULL,

                          remetente_id BIGINT NOT NULL,

                          conteudo TEXT NOT NULL,

                          enviado_em TIMESTAMP NOT NULL DEFAULT NOW(),

                          CONSTRAINT fk_mensagem_chat
                              FOREIGN KEY (chat_id)
                                  REFERENCES chat(id)
                                  ON DELETE CASCADE,

                          CONSTRAINT fk_mensagem_remetente
                              FOREIGN KEY (remetente_id)
                                  REFERENCES usuario(id)
                                  ON DELETE RESTRICT
);


-- ============================================================
-- ÍNDICES
-- ============================================================

CREATE INDEX idx_usuario_email
    ON usuario(email);

CREATE INDEX idx_usuario_tipo
    ON usuario(tipo);

CREATE INDEX idx_aluno_usuario
    ON aluno(usuario_id);

CREATE INDEX idx_aluno_academia
    ON aluno(academia_id);

CREATE INDEX idx_personal_usuario
    ON personal(usuario_id);

CREATE INDEX idx_nutricionista_usuario
    ON nutricionista(usuario_id);

CREATE INDEX idx_credencial_usuario
    ON credencial(usuario_id);

CREATE INDEX idx_personal_academia_personal
    ON personal_academia(personal_id);

CREATE INDEX idx_personal_academia_academia
    ON personal_academia(academia_id);

CREATE INDEX idx_refresh_token_usuario
    ON refresh_token(usuario_id);

CREATE INDEX idx_refresh_token_hash
    ON refresh_token(token_hash);

CREATE INDEX idx_refresh_token_expira_em
    ON refresh_token(expira_em);

CREATE INDEX idx_mensagem_chat
    ON mensagem(chat_id);

CREATE INDEX idx_mensagem_remetente
    ON mensagem(remetente_id);

CREATE INDEX idx_sessao_aluno
    ON sessao(aluno_id);

CREATE INDEX idx_sessao_profissional
    ON sessao(profissional_id);

CREATE INDEX idx_sessao_academia
    ON sessao(academia_id);

CREATE INDEX idx_sessao_data_hora
    ON sessao(data_hora);


-- ============================================================
-- VERIFICAÇÃO POSTGIS
-- ============================================================

SELECT PostGIS_Version();