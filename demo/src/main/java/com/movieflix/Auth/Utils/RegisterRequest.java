package com.movieflix.Auth.Utils;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // has all methods like toString hashcode etc required for data processing
@AllArgsConstructor
@NoArgsConstructor
@Builder //The @Builder annotation, provided by the Lombok library, can simplify object creation, reduce boilerplate code, and enhance the readability of your Java code.


public class RegisterRequest {


    private String name;
    private String email;
    private String username;
    private String password;
}
