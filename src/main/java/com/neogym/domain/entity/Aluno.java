package com.neogym.domain.entity;

import java.math.BigDecimal;

public class Aluno {

    private final Long       id;
    private final Long       usuarioId;
    private final Long       academiaId;
    private final BigDecimal peso;
    private final BigDecimal altura;
    private final Integer    metaAguaMl;

    private Aluno(Builder b) {
        this.id         = b.id;
        this.usuarioId  = b.usuarioId;
        this.academiaId = b.academiaId;
        this.peso       = b.peso;
        this.altura     = b.altura;
        this.metaAguaMl = b.metaAguaMl;
    }

    public Long       getId()         { return id; }
    public Long       getUsuarioId()  { return usuarioId; }
    public Long       getAcademiaId() { return academiaId; }
    public BigDecimal getPeso()       { return peso; }
    public BigDecimal getAltura()     { return altura; }
    public Integer    getMetaAguaMl() { return metaAguaMl; }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private Long       id;
        private Long       usuarioId;
        private Long       academiaId;
        private BigDecimal peso;
        private BigDecimal altura;
        private Integer    metaAguaMl;

        public Builder id(Long id)               { this.id = id; return this; }
        public Builder usuarioId(Long v)          { this.usuarioId = v; return this; }
        public Builder academiaId(Long v)         { this.academiaId = v; return this; }
        public Builder peso(BigDecimal v)         { this.peso = v; return this; }
        public Builder altura(BigDecimal v)       { this.altura = v; return this; }
        public Builder metaAguaMl(Integer v)      { this.metaAguaMl = v; return this; }

        public Aluno build() { return new Aluno(this); }
    }
}
