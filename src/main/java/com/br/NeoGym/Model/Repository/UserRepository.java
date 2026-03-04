package com.br.NeoGym.Model.Repository;

import com.br.NeoGym.Model.Entity.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, String> {

}