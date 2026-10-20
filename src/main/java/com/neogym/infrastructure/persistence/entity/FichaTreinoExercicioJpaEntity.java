package com.neogym.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ficha_treino_exercicio")
@Getter
@NoArgsConstructor
public class FichaTreinoExercicioJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "ficha_treino_id",
            nullable = false
    )
    private FichaTreinoJpaEntity fichaTreino;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false)
    private Integer series;

    @Column(nullable = false)
    private Integer repeticoes;

    @Column(nullable = false)
    private Integer ordem;

    public FichaTreinoExercicioJpaEntity(
            String nome,
            Integer series,
            Integer repeticoes,
            Integer ordem
    ) {
        this.nome = nome;
        this.series = series;
        this.repeticoes = repeticoes;
        this.ordem = ordem;
    }

    public void definirFichaTreino(
            FichaTreinoJpaEntity fichaTreino
    ) {
        this.fichaTreino = fichaTreino;
    }
}