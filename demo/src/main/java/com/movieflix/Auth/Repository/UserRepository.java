package com.movieflix.Auth.Repository;

import com.movieflix.Auth.Entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByEmail(String username);//it will generate query itself , JPA does this


    @Modifying //while doing any opeartion like update or delet, use these two annotations
    @Transactional
    @Query("update User u set u.email = ?1, u.password = ?2 where u.email = ?1")
    void updatePassword(String email,String password);
}
