
package com.neogym.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "notificacao_dispositivo",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_notificacao_dispositivo_token",
                        columnNames = "token"
                )
        },
        indexes = {
                @Index(
                        name = "idx_notificacao_dispositivo_usuario",
                        columnList = "usuario_id"
                ),
                @Index(
                        name = "idx_notificacao_dispositivo_firebase_usuario",
                        columnList = "firebase_uid, usuario_id"
                )
        }
)
public class NotificacaoDispositivoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "firebase_uid", nullable = false, length = 128)
    private String firebaseUid;

    @Column(name = "token", nullable = false, length = 4096)
    private String token;

    @Column(name = "plataforma", length = 30)
    private String plataforma;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    public NotificacaoDispositivoJpaEntity() {
    }

    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getFirebaseUid() {
        return firebaseUid;
    }

    public void setFirebaseUid(String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }
}
