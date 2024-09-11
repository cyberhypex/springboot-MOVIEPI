package com.movieflix.Auth.Entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ForgotPassword {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fpid;
    @Column(nullable = false)
    private Integer otp;

    @Column(nullable = false)
    private Date expirationTime;

    @OneToOne
    private User user;
}
