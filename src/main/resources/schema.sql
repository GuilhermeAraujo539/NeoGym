-- ── usuario ──────────────────────────────────────────────────────
CREATE TABLE usuario (
    id         BIGSERIAL    PRIMARY KEY,
    nome       VARCHAR(120) NOT NULL,
    email      VARCHAR(120) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    tipo       VARCHAR(20)  NOT NULL,
    ativo      BOOLEAN      NOT NULL DEFAULT TRUE,
    criado_em  TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- ── academia ─────────────────────────────────────────────────────
CREATE TABLE academia (
    id        BIGSERIAL    PRIMARY KEY,
    nome      VARCHAR(120) NOT NULL,
    cnpj      VARCHAR(18)  NOT NULL UNIQUE,
    latitude  VARCHAR(50),
    longitude VARCHAR(50),
    ativo     BOOLEAN      NOT NULL DEFAULT TRUE
);

-- ── aluno ────────────────────────────────────────────────────────
CREATE TABLE aluno (
    id           BIGSERIAL    PRIMARY KEY,
    usuario_id   BIGINT       NOT NULL UNIQUE REFERENCES usuario(id) ON DELETE CASCADE,
    academia_id  BIGINT       REFERENCES academia(id),
    peso         NUMERIC(5,2),
    altura       NUMERIC(4,2),
    meta_agua_ml INTEGER      DEFAULT 2000
);

-- ── personal ─────────────────────────────────────────────────────
CREATE TABLE personal (
    id          BIGSERIAL    PRIMARY KEY,
    usuario_id  BIGINT       NOT NULL UNIQUE REFERENCES usuario(id) ON DELETE CASCADE,
    cref        VARCHAR(20)  NOT NULL,
    estado_cref CHAR(2)      NOT NULL,
    status_cref VARCHAR(20)  NOT NULL DEFAULT 'PENDENTE'
);

-- ── nutricionista ─────────────────────────────────────────────────
CREATE TABLE nutricionista (
    id         BIGSERIAL    PRIMARY KEY,
    usuario_id BIGINT       NOT NULL UNIQUE REFERENCES usuario(id) ON DELETE CASCADE,
    crn        VARCHAR(20)  NOT NULL,
    estado_crn CHAR(2)      NOT NULL,
    status_crn VARCHAR(20)  NOT NULL DEFAULT 'PENDENTE'
);

-- ── credencial ────────────────────────────────────────────────────
CREATE TABLE credencial (
    id               BIGSERIAL    PRIMARY KEY,
    usuario_id       BIGINT       NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
    tipo             VARCHAR(10)  NOT NULL,
    arquivo_url      VARCHAR(255),
    status           VARCHAR(20)  NOT NULL DEFAULT 'PENDENTE',
    observacao_admin VARCHAR(500),
    avaliado_em      TIMESTAMP,
    avaliado_por_id  BIGINT       REFERENCES usuario(id)
);

-- ── personal_academia ─────────────────────────────────────────────
CREATE TABLE personal_academia (
    id          BIGSERIAL    PRIMARY KEY,
    personal_id BIGINT       NOT NULL REFERENCES personal(id) ON DELETE CASCADE,
    academia_id BIGINT       NOT NULL REFERENCES academia(id) ON DELETE CASCADE,
    status      VARCHAR(20)  NOT NULL DEFAULT 'PENDENTE',
    data_inicio DATE,
    UNIQUE (personal_id, academia_id)
);

-- ── refresh_token ─────────────────────────────────────────────────
CREATE TABLE refresh_token (
    id         BIGSERIAL    PRIMARY KEY,
    usuario_id BIGINT       NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
    token_hash VARCHAR(255) NOT NULL UNIQUE,
    expira_em  TIMESTAMP    NOT NULL,
    revogado   BOOLEAN      NOT NULL DEFAULT FALSE,
    criado_em  TIMESTAMP    NOT NULL DEFAULT NOW(),
    ip_origem  VARCHAR(45),
    user_agent VARCHAR(255)
);

-- ── ficha_treino ──────────────────────────────────────────────────
CREATE TABLE ficha_treino (
    id          BIGSERIAL PRIMARY KEY,
    aluno_id    BIGINT    NOT NULL REFERENCES aluno(id) ON DELETE CASCADE,
    personal_id BIGINT    NOT NULL REFERENCES personal(id),
    descricao   TEXT      NOT NULL,
    criada_em   TIMESTAMP NOT NULL DEFAULT NOW()
);

-- ── registro_agua ─────────────────────────────────────────────────
CREATE TABLE registro_agua (
    id            BIGSERIAL PRIMARY KEY,
    aluno_id      BIGINT    NOT NULL REFERENCES aluno(id) ON DELETE CASCADE,
    data          DATE      NOT NULL,
    quantidade_ml INTEGER   NOT NULL,
    UNIQUE (aluno_id, data)
);

-- ── sessao ────────────────────────────────────────────────────────
CREATE TABLE sessao (
    id              BIGSERIAL    PRIMARY KEY,
    aluno_id        BIGINT       NOT NULL REFERENCES aluno(id),
    profissional_id BIGINT       NOT NULL REFERENCES usuario(id),
    academia_id     BIGINT       REFERENCES academia(id),
    tipo            VARCHAR(15)  NOT NULL,
    data_hora       TIMESTAMP    NOT NULL,
    status          VARCHAR(15)  NOT NULL DEFAULT 'AGENDADA'
);

-- ── chat ──────────────────────────────────────────────────────────
CREATE TABLE chat (
    id        BIGSERIAL   PRIMARY KEY,
    tipo      VARCHAR(10) NOT NULL DEFAULT 'PRIVADO',
    criado_em TIMESTAMP   NOT NULL DEFAULT NOW()
);

-- ── chat_participante ─────────────────────────────────────────────
CREATE TABLE chat_participante (
    id         BIGSERIAL PRIMARY KEY,
    chat_id    BIGINT    NOT NULL REFERENCES chat(id) ON DELETE CASCADE,
    usuario_id BIGINT    NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
    UNIQUE (chat_id, usuario_id)
);

-- ── mensagem ──────────────────────────────────────────────────────
CREATE TABLE mensagem (
    id           BIGSERIAL PRIMARY KEY,
    chat_id      BIGINT    NOT NULL REFERENCES chat(id) ON DELETE CASCADE,
    remetente_id BIGINT    NOT NULL REFERENCES usuario(id),
    conteudo     TEXT      NOT NULL,
    enviado_em   TIMESTAMP NOT NULL DEFAULT NOW()
);

-- ================================================================
-- ÍNDICES
-- ================================================================
CREATE INDEX idx_usuario_email             ON usuario(email);
CREATE INDEX idx_aluno_usuario             ON aluno(usuario_id);
CREATE INDEX idx_personal_usuario          ON personal(usuario_id);
CREATE INDEX idx_nutricionista_usuario     ON nutricionista(usuario_id);
CREATE INDEX idx_credencial_usuario_id     ON credencial(usuario_id);
CREATE INDEX idx_credencial_status         ON credencial(status);
CREATE INDEX idx_credencial_avaliado_por   ON credencial(avaliado_por_id);
CREATE INDEX idx_personal_academia_status  ON personal_academia(status);
CREATE INDEX idx_refresh_token_usuario     ON refresh_token(usuario_id);
CREATE INDEX idx_refresh_token_hash        ON refresh_token(token_hash);
CREATE INDEX idx_refresh_token_expira_em   ON refresh_token(expira_em);
CREATE INDEX idx_sessao_aluno              ON sessao(aluno_id);
CREATE INDEX idx_sessao_profissional       ON sessao(profissional_id);
CREATE INDEX idx_mensagem_chat             ON mensagem(chat_id);
CREATE INDEX idx_mensagem_enviado_em       ON mensagem(enviado_em);
