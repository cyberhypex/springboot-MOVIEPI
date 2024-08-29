package com.movieflix.Auth.services;


import com.movieflix.Auth.Entities.RefreshToken;
import com.movieflix.Auth.Entities.User;
import com.movieflix.Auth.Repository.RefreshTokenRepository;
import com.movieflix.Auth.Repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;


    public RefreshTokenService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }


    public RefreshToken createRefreshToken(String username){
      User user=  userRepository.findByUserName(username).orElseThrow(()->new UsernameNotFoundException("User Not Found with email:"+username));
        RefreshToken refreshToken=user.getRefreshToken();
        if(refreshToken==null){
            long refreshTokenValidity=5*60*60*10000;
            refreshToken=RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusMillis(refreshTokenValidity))
                    .user(user)
                    .build();
            refreshTokenRepository.save(refreshToken);
        }
        return refreshToken;
    }

    public  RefreshToken verifyRefreshToken(String refreshToken){
       RefreshToken refToken= refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(()->new RuntimeException("Refresh Token does'nt exists"));

       if(refToken.getExpirationTime().compareTo(Instant.now())<0){
           refreshTokenRepository.delete(refToken);
           throw new RuntimeException("RefreshToken expired");
       }
       return refToken;
    }
}