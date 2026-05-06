package com.neogym.domain.entity;

import com.neogym.domain.enums.StatusCredencial;

public class Nutricionista {

    private final Long             id;
    private final Long             usuarioId;
    private final String           crn;
    private final String           estadoCrn;
    private final StatusCredencial statusCrn;

    private Nutricionista(Builder b) {
        this.id        = b.id;
        this.usuarioId = b.usuarioId;
        this.crn       = b.crn;
        this.estadoCrn = b.estadoCrn;
        this.statusCrn = b.statusCrn;
    }

    public Long             getId()        { return id; }
    public Long             getUsuarioId() { return usuarioId; }
    public String           getCrn()       { return crn; }
    public String           getEstadoCrn() { return estadoCrn; }
    public StatusCredencial getStatusCrn() { return statusCrn; }

    public boolean isAprovado() { return StatusCredencial.APROVADO.equals(statusCrn); }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private Long             id;
        private Long             usuarioId;
        private String           crn;
        private String           estadoCrn;
        private StatusCredencial statusCrn;

        public Builder id(Long id)                    { this.id = id; return this; }
        public Builder usuarioId(Long v)              { this.usuarioId = v; return this; }
        public Builder crn(String v)                  { this.crn = v; return this; }
        public Builder estadoCrn(String v)            { this.estadoCrn = v; return this; }
        public Builder statusCrn(StatusCredencial v)  { this.statusCrn = v; return this; }

        public Nutricionista build() { return new Nutricionista(this); }
    }
}
