package com.neogym.infrastructure.security.service;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class AuthenticatedUser implements UserDetails {

    private final Long usuarioId;
    private final String nome;
    private final String email;
    private final String senha;
    private final boolean ativo;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthenticatedUser(
            Long usuarioId,
            String nome,
            String email,
            String senha,
            boolean ativo,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.ativo = ativo;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return ativo;
    }
}