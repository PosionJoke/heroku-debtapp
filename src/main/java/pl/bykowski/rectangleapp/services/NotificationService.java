package pl.bykowski.rectangleapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class NotificationService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public NotificationService(JavaMailSender javaMailSender) {
        this.javaMailSender = Objects.requireNonNull(javaMailSender, "javaMailSender must be not null");
    }

    public boolean sendNotification(String email, String code) throws MailException {
        //send email
        try{
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(email);
            mail.setFrom("rostkowskiadrian00@gmail.com");
            mail.setSubject("Welcome to my app :D");
            mail.setText("AuthenticationCode = " + code);
            javaMailSender.send(mail);
            return true;
        }catch (MailException mEx){
            mEx.printStackTrace();
            return false;
        }
    }
}