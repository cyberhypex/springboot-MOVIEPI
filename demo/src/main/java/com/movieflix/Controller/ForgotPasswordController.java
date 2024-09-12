package com.movieflix.Controller;


import com.movieflix.Auth.Entities.ForgotPassword;
import com.movieflix.Auth.Entities.User;
import com.movieflix.Auth.Repository.ForgotPasswordRepository;
import com.movieflix.Auth.Repository.UserRepository;
import com.movieflix.Auth.Utils.ChangePassword;
import com.movieflix.DTO.MailBody;
import com.movieflix.Service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/forgotPassword")
public class ForgotPasswordController {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final ForgotPasswordRepository forgotPasswordRepository;

    public ForgotPasswordController(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder, ForgotPasswordRepository forgotPasswordRepository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
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



    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email){
        User user=userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("Please enter correct e-mail"));
        //check if user with this email exists or not
        ForgotPassword fp=forgotPasswordRepository.findByOtpAndUser(otp,user).orElseThrow(()->new RuntimeException("Invalid Otp for email:"+email));
        if(fp.getExpirationTime().before(Date.from(Instant.now()))){ //checking if otp is non expired till now
             forgotPasswordRepository.deleteById(fp.getFpid()); //delete so that it does not collide
             return new ResponseEntity<>("Otp has expired", HttpStatus.EXPECTATION_FAILED);

        }
        return ResponseEntity.ok("Otp verified");
    }


    @PostMapping("/changePassword/{email}")
    public ResponseEntity<String> changePasswordController(
            @RequestBody ChangePassword changePassword,
            @PathVariable String email){
        if(!Objects.equals(changePassword.password(),changePassword.repeatPassword())){
    return  new ResponseEntity<>("Please enter password again",HttpStatus.EXPECTATION_FAILED);

        }
        String encodedPassword=passwordEncoder.encode(changePassword.password());
        userRepository.updatePassword(email,encodedPassword);
        return ResponseEntity.ok("Password is changed successfully");

    }


    private Integer otpGenerator(){
        Random random=new Random();
        return random.nextInt(100_000,999_999); //min & max
    }
}
