package pl.bykowski.rectangleapp.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class NotificationService {
    private static final Logger logger = Logger.getLogger(NotificationService.class);
    private static final String APP_MAIL = "rostkowskiadrian00@gmail.com";

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
            mail.setFrom(APP_MAIL);
            mail.setSubject("Welcome to my app :D");
            mail.setText("AuthenticationCode = " + code);
            javaMailSender.send(mail);
            logger.debug("Mail has been sent" +
                    "\nfrom : " + APP_MAIL +
                    "\nto : " + email);

            return true;
        }catch (MailException mEx){
            mEx.printStackTrace();
            return false;
        }
    }
}