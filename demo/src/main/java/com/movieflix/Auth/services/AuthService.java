package com.movieflix.Auth.services;


import com.movieflix.Auth.Entities.User;
import com.movieflix.Auth.Entities.UserRole;
import com.movieflix.Auth.Repository.UserRepository;
import com.movieflix.Auth.Utils.AuthResponse;
import com.movieflix.Auth.Utils.LoginRequest;
import com.movieflix.Auth.Utils.RegisterRequest;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final JwtService jwtService;
    private  final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepository, JwtService jwtService, RefreshTokenService refreshTokenService, AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest registerRequest){
        var user= User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .build();

        User savedUser=userRepository.save(user);
        var accessToken=jwtService.generateToken(savedUser);
        var refreshToken=refreshTokenService.createRefreshToken(savedUser.getEmail());//create refresh token using email
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();

    }

    public AuthResponse login(LoginRequest loginRequest){


       authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()

                )

        );
       var user=userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()->new UsernameNotFoundException("No USer found"));

       var accessToken=jwtService.generateToken(user);
       var refreshToken=refreshTokenService.createRefreshToken(loginRequest.getEmail());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();

    }
}
