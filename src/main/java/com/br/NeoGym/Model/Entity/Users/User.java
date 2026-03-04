package com.br.NeoGym.Model.Entity.Users;

import com.br.NeoGym.Model.Entity.Enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "ID único do usuário", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @Column(name = "name", nullable = false)
    @Schema(description = "Nome do usuário")
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    @Schema(description = "Email do usuário")
    private String email;

    @Column(name = "passwordHash", unique = true, nullable = false)
    @Schema(description = "Senha do usuário codificada com hash code")
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    @Schema(description = "Tipagem de qual grupo o usuário pertence", example = "ALUNO")
    private UserType userType;

    @Column(name = "active")
    @Schema(description = "comportamento durante a verificação de documento documentação", example = "True se a documentação foi revisada e aprovada, False caso seja recusada ou não foi verificada ainda")
    private boolean ativo;

    @Column(name = "criated_at")
    @Schema(description = "Data em que o usuário foi criado")
    private Instant criatedAt;
}
