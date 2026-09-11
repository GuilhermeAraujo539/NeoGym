package com.neogym.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "academia")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademiaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 18)
    private String cnpj;

    @Column(name = "localizacao", columnDefinition = "geography(Point,4326)")
    private Point localizacao;

    @Column(nullable = false)
    private boolean ativo;
}