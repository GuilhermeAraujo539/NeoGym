package com.br.neogym.domain.entities;

import com.br.neogym.domain.enums.UserRole;

import java.time.Instant;
import java.util.UUID;

public class User {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private UserRole userRole;
    private boolean active;
    private Instant createdAt;

    public User(String name, String email, String password, UserRole userRole) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.active = true;
        this.createdAt = Instant.now();
    }

    public User(UUID id, String name, String email, String password,
                UserRole userRole, boolean active, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.active = active;
        this.createdAt = createdAt;
    }

    public void deactivate(){
        if (!this.active){
            throw new  RuntimeException("User is deactivated already");
        }
        this.active = false;
    }

    public void actvate(){
        if (this.active){
            throw new  RuntimeException("User is activated already");
        }
        this.active = true;
    }

    public void changePassword(String newPassword){
        validatePassword(newPassword);
        this.password = newPassword;
    }


    private void validateEmail(String email){
        if (email.isEmpty() || !email.contains("@")){
            throw new  IllegalArgumentException("Invalid email");
        }
    }



    private void validateName(String name){
        if (name.isEmpty() || !name.contains("@")){
            throw new  IllegalArgumentException("Invalid name");
        }
    }
}