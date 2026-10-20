package com.neogym.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "personal_foto")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalFotoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_id", nullable = false)
    private PersonalJpaEntity personal;

    @Column(name = "dados", nullable = false, columnDefinition = "bytea")
    private byte[] dados;

    @Column(name = "content_type", nullable = false, length = 100)
    private String contentType;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;
}