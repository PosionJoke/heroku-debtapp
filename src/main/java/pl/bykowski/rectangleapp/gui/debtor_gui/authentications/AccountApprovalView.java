package pl.bykowski.rectangleapp.gui.debtor_gui.authentications;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.mail.MailException;
import pl.bykowski.rectangleapp.services.DebtorService;
import pl.bykowski.rectangleapp.services.NotificationService;

@Route(value = AccountApprovalView.ROUTE)
public class AccountApprovalView extends VerticalLayout {

    public static final String ROUTE = "account-approval";

    private DebtorService debtorService;
    private NotificationService notificationService;


    public AccountApprovalView(DebtorService debtorService, NotificationService notificationService) {

        this.debtorService = debtorService;
        this.notificationService = notificationService;


        String email = debtorService.testGetUserEmail();

        Button button = new Button(email);

        add(button);

        try {
            notificationService.sendNotification(debtorService.testGetUserEmail());
        } catch (MailException e) {
            e.printStackTrace();
        }

    }
}
