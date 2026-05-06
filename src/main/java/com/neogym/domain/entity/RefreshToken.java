package com.neogym.domain.entity;

import java.time.LocalDateTime;

public class RefreshToken {

    private final Long          id;
    private final Long          usuarioId;
    private final String        tokenHash;
    private final LocalDateTime expiraEm;
    private final boolean       revogado;
    private final LocalDateTime criadoEm;
    private final String        ipOrigem;
    private final String        userAgent;

    private RefreshToken(Builder b) {
        this.id        = b.id;
        this.usuarioId = b.usuarioId;
        this.tokenHash = b.tokenHash;
        this.expiraEm  = b.expiraEm;
        this.revogado  = b.revogado;
        this.criadoEm  = b.criadoEm;
        this.ipOrigem  = b.ipOrigem;
        this.userAgent = b.userAgent;
    }

    public Long          getId()        { return id; }
    public Long          getUsuarioId() { return usuarioId; }
    public String        getTokenHash() { return tokenHash; }
    public LocalDateTime getExpiraEm()  { return expiraEm; }
    public boolean       isRevogado()   { return revogado; }
    public LocalDateTime getCriadoEm()  { return criadoEm; }
    public String        getIpOrigem()  { return ipOrigem; }
    public String        getUserAgent() { return userAgent; }

    public boolean estaExpirado() { return LocalDateTime.now().isAfter(expiraEm); }
    public boolean estaValido()   { return !revogado && !estaExpirado(); }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private Long          id;
        private Long          usuarioId;
        private String        tokenHash;
        private LocalDateTime expiraEm;
        private boolean       revogado;
        private LocalDateTime criadoEm;
        private String        ipOrigem;
        private String        userAgent;

        public Builder id(Long id)               { this.id = id; return this; }
        public Builder usuarioId(Long v)          { this.usuarioId = v; return this; }
        public Builder tokenHash(String v)        { this.tokenHash = v; return this; }
        public Builder expiraEm(LocalDateTime v)  { this.expiraEm = v; return this; }
        public Builder revogado(boolean v)        { this.revogado = v; return this; }
        public Builder criadoEm(LocalDateTime v)  { this.criadoEm = v; return this; }
        public Builder ipOrigem(String v)         { this.ipOrigem = v; return this; }
        public Builder userAgent(String v)        { this.userAgent = v; return this; }

        public RefreshToken build() { return new RefreshToken(this); }
    }
}
