package pl.bykowski.rectangleapp.services;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Log4j
@Service
public class NotificationService {
    private static final String APP_MAIL = "rostkowskiadrian00@gmail.com";

    private final JavaMailSender javaMailSender;

    @Autowired
    public NotificationService(JavaMailSender javaMailSender) {
        this.javaMailSender = Objects.requireNonNull(javaMailSender, "javaMailSender must be not null");
    }

    boolean sendNotification(String email, String code) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(email);
            mail.setFrom(APP_MAIL);
            mail.setSubject("Welcome to my app :D");
            mail.setText("AuthenticationCode = " + code);
            javaMailSender.send(mail);

            log.debug(String.format("Mail has been sent, from : [%s], to : [%s]", APP_MAIL, email));

            return true;
        } catch (MailException mEx) {
            log.error("Mail not sent, error message : ", mEx);
            return false;
        }
    }
}