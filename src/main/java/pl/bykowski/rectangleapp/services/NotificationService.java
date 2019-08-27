package pl.bykowski.rectangleapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private JavaMailSender javaMailSender;

    @Autowired
    public NotificationService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendNotification(String email, String code) throws MailException {
        //send email
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom("rostkowskiadrian00@gmail.com");
        mail.setSubject("Rarity is best Pony!");
        mail.setText("Yep, Rararara is best pony\nAuthenticationCode = " + code);

        javaMailSender.send(mail);
    }

}