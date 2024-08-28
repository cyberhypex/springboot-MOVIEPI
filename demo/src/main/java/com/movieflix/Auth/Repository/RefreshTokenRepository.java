package com.movieflix.Auth.Repository;

import com.movieflix.Auth.Entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Integer> {
}
