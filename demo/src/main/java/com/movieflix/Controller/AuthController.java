package com.movieflix.Controller;


import com.movieflix.Auth.Entities.RefreshToken;
import com.movieflix.Auth.Entities.User;
import com.movieflix.Auth.Utils.AuthResponse;
import com.movieflix.Auth.Utils.LoginRequest;
import com.movieflix.Auth.Utils.RefreshTokenRequest;
import com.movieflix.Auth.Utils.RegisterRequest;
import com.movieflix.Auth.services.AuthService;
import com.movieflix.Auth.services.JwtService;
import com.movieflix.Auth.services.RefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    private  final JwtService jwtService;


    public AuthController(AuthService authService, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }



    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(authService.register(registerRequest));
    }

   @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }



    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
      RefreshToken refreshToken= refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());//verify

        //get the user

        User user=refreshToken.getUser();

        //generate access token
        String accessToken= jwtService.generateToken(user);

        return  ResponseEntity.ok(AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build()




        );
    }




}
