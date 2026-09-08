package com.neogym.infrastructure.security.service;

import com.neogym.infrastructure.persistence.repository.UsuarioJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioJpaRepository usuarioJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        return usuarioJpaRepository.findByEmail(email)
                .map(u -> new AuthenticatedUser(
                        u.getId(),
                        u.getNome(),
                        u.getEmail(),
                        u.getSenhaHash(),
                        u.isAtivo(),
                        List.of(
                                new SimpleGrantedAuthority(
                                        "ROLE_" + u.getTipo()
                                )
                        )
                ))
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuário não encontrado: " + email
                        )
                );
    }
}