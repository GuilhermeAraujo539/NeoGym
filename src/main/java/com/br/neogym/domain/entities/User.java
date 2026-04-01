package com.br.neogym.aplication.entities;

import com.br.neogym.aplication.enums.UserRole;

import java.security.Timestamp;
import java.util.UUID;

public class User {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private UserRole userRole;
    private boolean status;
    private Timestamp createdAt;

    public User(UUID id, String name, String email, String password, UserRole userRole, boolean status, Timestamp createdAt) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.status = status;
        this.createdAt = createdAt;
    }
}
