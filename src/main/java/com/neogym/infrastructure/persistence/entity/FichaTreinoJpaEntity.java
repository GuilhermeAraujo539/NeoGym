package com.neogym.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ficha_treino")
@Getter
@NoArgsConstructor
public class FichaTreinoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aluno_id", nullable = false)
    private Long alunoId;

    @Column(name = "personal_id", nullable = false)
    private Long personalId;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(length = 120)
    private String objetivo;

    private Integer duracao;

    @Column(name = "criada_em", nullable = false)
    private LocalDateTime criadaEm;

    @OneToMany(
            mappedBy = "fichaTreino",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("ordem ASC")
    private List<FichaTreinoExercicioJpaEntity> exercicios =
            new ArrayList<>();

    public FichaTreinoJpaEntity(
            Long alunoId,
            Long personalId,
            String nome,
            String objetivo,
            Integer duracao,
            LocalDateTime criadaEm
    ) {
        this.alunoId = alunoId;
        this.personalId = personalId;
        this.nome = nome;
        this.objetivo = objetivo;
        this.duracao = duracao;
        this.criadaEm = criadaEm;
    }

    public void adicionarExercicio(
            FichaTreinoExercicioJpaEntity exercicio
    ) {
        exercicios.add(exercicio);
        exercicio.definirFichaTreino(this);
    }
}