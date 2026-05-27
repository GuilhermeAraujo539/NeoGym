package com.neogym.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "personal_academia")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class PersonalAcademiaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_id", nullable = false)
    private PersonalJpaEntity personal;

    @Column(name = "academia_id", nullable = false)
    private Long academiaId;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;
}
