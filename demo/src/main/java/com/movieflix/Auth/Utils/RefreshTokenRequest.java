package com.movieflix.Auth.Utils;


import lombok.Data;

@Data
public class RefreshTokenRequest {
    //will be used when we want new jwt token

    private String refreshToken;

}
