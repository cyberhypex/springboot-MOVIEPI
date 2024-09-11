package com.movieflix.Service;


import com.movieflix.DTO.MailBody;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service // this class will define the template of the email
public class EmailService {

    private final JavaMailSender javaMailSender; //responsible for sending mail

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleMessage(MailBody mailBody){
        SimpleMailMessage message=new SimpleMailMessage();//we need to use this class becuase java mailsender needs a object of this type

        message.setTo(mailBody.to());
        message.setFrom("");
        message.setSubject(mailBody.subject());
        message.setText(mailBody.text());

        javaMailSender.send(message);
    }
}
