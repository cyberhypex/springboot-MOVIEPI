package com.movieflix.Controller;


import com.movieflix.Auth.Entities.ForgotPassword;
import com.movieflix.Auth.Entities.User;
import com.movieflix.Auth.Repository.ForgotPasswordRepository;
import com.movieflix.Auth.Repository.UserRepository;
import com.movieflix.DTO.MailBody;
import com.movieflix.Service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping("/forgotPassword")
public class ForgotPasswordController {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ForgotPasswordRepository forgotPasswordRepository;

    public ForgotPasswordController(UserRepository userRepository, EmailService emailService, ForgotPasswordRepository forgotPasswordRepository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.forgotPasswordRepository = forgotPasswordRepository;
    }


    //send mail for email verification
    public ResponseEntity<String> verifyEmail(@PathVariable String email){
        User user=userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("Please enter correct e-mail"));

        int otp=otpGenerator();
        MailBody mailBody= MailBody.builder()
                .to(email)
                .text("This is the OTP for you password reset:"+""+otp)
                .subject("Forgot Password request").build();
        //creat forgot password opbject also, for validation and keeping otp
        ForgotPassword fp=ForgotPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis()+70*1000))
                .user(user)



                .build();
        emailService.sendSimpleMessage(mailBody);
        forgotPasswordRepository.save(fp);

        return ResponseEntity.ok("Email is sent ");
    }


    private Integer otpGenerator(){
        Random random=new Random();
        return random.nextInt(100_000,999_999); //min & max
    }
}
