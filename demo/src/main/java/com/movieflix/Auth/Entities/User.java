package com.movieflix.Auth.Entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter

public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

   @NotBlank(message = "This can't be empty")
    private String name;
    @NotBlank(message = "This can't be empty")
    private String userName;
    @NotBlank(message = "This can't be empty")
    @Column(unique = true)
    @Email(message = "Please enter email in perfect format")// helps check if it mainatisn email regex

    private String email;
    @NotBlank(message = "This can't be empty")
    @Size(min = 5,message ="Password must be atleast 5 characters" )
    private String password;


    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private boolean isEnabled; // by default set to true
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;





    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

       return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
