package com.neogym.domain.entity;

import com.neogym.domain.enums.StatusCredencial;
import com.neogym.domain.enums.TipoCredencial;

import java.time.LocalDateTime;

public class Credencial {

    private final Long             id;
    private final Long             usuarioId;
    private final TipoCredencial   tipo;
    private final String           arquivoUrl;
    private final StatusCredencial status;
    private final String           observacaoAdmin;
    private final LocalDateTime    criadoEm;
    private final LocalDateTime    avaliadoEm;
    private final Long             avaliadoPorId;

    private Credencial(Builder b) {
        this.id              = b.id;
        this.usuarioId       = b.usuarioId;
        this.tipo            = b.tipo;
        this.arquivoUrl      = b.arquivoUrl;
        this.status          = b.status;
        this.observacaoAdmin = b.observacaoAdmin;
        this.criadoEm        = b.criadoEm;
        this.avaliadoEm      = b.avaliadoEm;
        this.avaliadoPorId   = b.avaliadoPorId;
    }

    public Long             getId()              { return id; }
    public Long             getUsuarioId()       { return usuarioId; }
    public TipoCredencial   getTipo()            { return tipo; }
    public String           getArquivoUrl()      { return arquivoUrl; }
    public StatusCredencial getStatus()          { return status; }
    public String           getObservacaoAdmin() { return observacaoAdmin; }
    public LocalDateTime    getCriadoEm()        { return criadoEm; }
    public LocalDateTime    getAvaliadoEm()      { return avaliadoEm; }
    public Long             getAvaliadoPorId()   { return avaliadoPorId; }

    public boolean isPendente()  { return StatusCredencial.PENDENTE.equals(status); }
    public boolean isAprovado()  { return StatusCredencial.APROVADO.equals(status); }
    public boolean isRejeitado() { return StatusCredencial.REJEITADO.equals(status); }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private Long             id;
        private Long             usuarioId;
        private TipoCredencial   tipo;
        private String           arquivoUrl;
        private StatusCredencial status;
        private String           observacaoAdmin;
        private LocalDateTime    criadoEm;
        private LocalDateTime    avaliadoEm;
        private Long             avaliadoPorId;

        public Builder id(Long v)                    { this.id = v; return this; }
        public Builder usuarioId(Long v)             { this.usuarioId = v; return this; }
        public Builder tipo(TipoCredencial v)        { this.tipo = v; return this; }
        public Builder arquivoUrl(String v)          { this.arquivoUrl = v; return this; }
        public Builder status(StatusCredencial v)    { this.status = v; return this; }
        public Builder observacaoAdmin(String v)     { this.observacaoAdmin = v; return this; }
        public Builder criadoEm(LocalDateTime v)     { this.criadoEm = v; return this; }
        public Builder avaliadoEm(LocalDateTime v)   { this.avaliadoEm = v; return this; }
        public Builder avaliadoPorId(Long v)         { this.avaliadoPorId = v; return this; }

        public Credencial build() { return new Credencial(this); }
    }
}
