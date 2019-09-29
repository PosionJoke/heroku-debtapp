package pl.bykowski.rectangleapp.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;
    @Mock
    private JavaMailSender javaMailSender;

    @Test
    public void should_return_true_when_email_was_correct() {
        //given
        String email = "example@email.com";
        String code = "1234";
        //when
        boolean returnValue = notificationService.sendNotification(email, code);
        //then
        assertThat(returnValue).isEqualTo(true);
    }

    @Test
    public void should_return_false_when_email_was_wrong() {
        //given
        String wrongEmail = "1111";
        String code = "1234";
        given(notificationService.sendNotification(wrongEmail, code)).willThrow(MailSendException.class);
        //when
        boolean returnValue = notificationService.sendNotification(wrongEmail, code);
        //then
        assertThat(returnValue).isEqualTo(false);
    }

}
