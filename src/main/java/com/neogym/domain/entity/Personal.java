package com.neogym.domain.entity;

import com.neogym.domain.enums.StatusCredencial;

public class Personal {

    private final Long             id;
    private final Long             usuarioId;
    private final String           cref;
    private final String           estadoCref;
    private final StatusCredencial statusCref;

    private Personal(Builder b) {
        this.id         = b.id;
        this.usuarioId  = b.usuarioId;
        this.cref       = b.cref;
        this.estadoCref = b.estadoCref;
        this.statusCref = b.statusCref;
    }

    public Long             getId()         { return id; }
    public Long             getUsuarioId()  { return usuarioId; }
    public String           getCref()       { return cref; }
    public String           getEstadoCref() { return estadoCref; }
    public StatusCredencial getStatusCref() { return statusCref; }

    public boolean isAprovado() { return StatusCredencial.APROVADO.equals(statusCref); }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private Long             id;
        private Long             usuarioId;
        private String           cref;
        private String           estadoCref;
        private StatusCredencial statusCref;

        public Builder id(Long id)                     { this.id = id; return this; }
        public Builder usuarioId(Long v)               { this.usuarioId = v; return this; }
        public Builder cref(String v)                  { this.cref = v; return this; }
        public Builder estadoCref(String v)            { this.estadoCref = v; return this; }
        public Builder statusCref(StatusCredencial v)  { this.statusCref = v; return this; }

        public Personal build() { return new Personal(this); }
    }
}
