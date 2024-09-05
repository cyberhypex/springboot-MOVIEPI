package com.movieflix.Auth.Utils;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AuthResponse {
    //this class will give response
    private String accessToken;
    private String refreshToken;
}
