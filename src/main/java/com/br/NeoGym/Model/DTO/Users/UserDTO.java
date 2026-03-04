package com.br.NeoGym.Model.DTO.Users;

import com.br.NeoGym.Model.Entity.Enums.UserType;

import java.time.Instant;

public record UserDTO(
        String id,
        String name,
        String email,
        String password_hash,
        UserType userType,
        boolean ative,
        Instant criated_at
) {

}
