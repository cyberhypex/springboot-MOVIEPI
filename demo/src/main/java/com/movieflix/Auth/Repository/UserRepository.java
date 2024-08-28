package com.movieflix.Auth.Repository;

import com.movieflix.Auth.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByUserName(String username);//it will generate query itself , JPA does this

}
