package com.movieflix.Auth.Repository;

import com.movieflix.Auth.Entities.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword,Integer> {


}
