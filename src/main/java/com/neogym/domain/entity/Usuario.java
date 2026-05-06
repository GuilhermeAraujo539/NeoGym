package com.neogym.domain.entity;

import com.neogym.domain.enums.TipoUsuario;

import java.time.LocalDateTime;


public class Usuario {

    private final Long          id;
    private final String        nome;
    private final String        email;
    private final String        senhaHash;
    private final TipoUsuario   tipo;
    private final boolean       ativo;
    private final LocalDateTime criadoEm;

    private Usuario(Builder b) {
        this.id        = b.id;
        this.nome      = b.nome;
        this.email     = b.email;
        this.senhaHash = b.senhaHash;
        this.tipo      = b.tipo;
        this.ativo     = b.ativo;
        this.criadoEm  = b.criadoEm;
    }

    public Long          getId()        { return id; }
    public String        getNome()      { return nome; }
    public String        getEmail()     { return email; }
    public String        getSenhaHash() { return senhaHash; }
    public TipoUsuario   getTipo()      { return tipo; }
    public boolean       isAtivo()      { return ativo; }
    public LocalDateTime getCriadoEm()  { return criadoEm; }

    public boolean isProfissional() {
        return tipo == TipoUsuario.PERSONAL || tipo == TipoUsuario.NUTRICIONISTA;
    }

    public boolean isAdmin() {
        return tipo == TipoUsuario.ADMIN;
    }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private Long          id;
        private String        nome;
        private String        email;
        private String        senhaHash;
        private TipoUsuario   tipo;
        private boolean       ativo;
        private LocalDateTime criadoEm;

        public Builder id(Long id)               { this.id = id; return this; }
        public Builder nome(String nome)          { this.nome = nome; return this; }
        public Builder email(String email)        { this.email = email; return this; }
        public Builder senhaHash(String h)        { this.senhaHash = h; return this; }
        public Builder tipo(TipoUsuario tipo)     { this.tipo = tipo; return this; }
        public Builder ativo(boolean ativo)       { this.ativo = ativo; return this; }
        public Builder criadoEm(LocalDateTime dt) { this.criadoEm = dt; return this; }

        public Usuario build() { return new Usuario(this); }
    }
}
