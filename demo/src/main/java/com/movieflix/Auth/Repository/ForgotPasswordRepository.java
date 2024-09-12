package com.movieflix.Auth.Repository;

import com.movieflix.Auth.Entities.ForgotPassword;
import com.movieflix.Auth.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword,Integer> {

    @Query("select fp from ForgotPassword fp where fp.otp=?1 and fp.user=?2")
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);

}
