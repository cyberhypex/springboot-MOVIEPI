package com.movieflix.Auth.Repository;


import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tokenID;


    @Column(nullable = false)
    private Instant expirationTime;

    @Column(nullable = false,length =500 )
    @NotBlank(message = "Please Enter refresh token value")
    private String refreshToken;
    @OneToOne
    private User user;
}
