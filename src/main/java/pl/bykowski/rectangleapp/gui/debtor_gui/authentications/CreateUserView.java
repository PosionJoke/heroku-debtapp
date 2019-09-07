package pl.bykowski.rectangleapp.gui.debtor_gui.authentications;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.bykowski.rectangleapp.repositories.DebtorUserRepo;
import pl.bykowski.rectangleapp.model.DebtorUser;
import pl.bykowski.rectangleapp.services.NotificationService;

@Route(value = "create-user")
public class CreateUserView extends VerticalLayout {

    private DebtorUserRepo debtorUserRepo;
    private PasswordEncoder passwordEncoder;
    private NotificationService notificationService;

    private Button addNewUserButton;
    private Button activeUserButton;
    private Button logInButton;

    private TextField userNameTextField;
    private TextField emailTextField;
    private TextField verificationCodeTextField;

    private PasswordField userPasswordTextField1;
    private PasswordField userPasswordTextField2;

    private String authenticationCode;

    private DebtorUser newDebtorUser;

    private Notification notification;

    public CreateUserView(DebtorUserRepo debtorUserRepo, PasswordEncoder passwordEncoder, NotificationService notificationService) {
        this.debtorUserRepo = debtorUserRepo;
        this.passwordEncoder = passwordEncoder;
        this.notificationService = notificationService;

        this.userNameTextField = new TextField("User Name");
        this.emailTextField = new TextField("Email");
        this.verificationCodeTextField = new TextField("Paste Code from your email");
        this.verificationCodeTextField.setVisible(false);

        this.userPasswordTextField1 = new PasswordField("Password");
        this.userPasswordTextField2 = new PasswordField("Repeat password");

        this.addNewUserButton = new Button("Create Account!");
        this.activeUserButton = new Button("Active account by code from email");
        this.activeUserButton.setVisible(false);
        this.logInButton = new Button("Log In");
        this.logInButton.setVisible(false);

        this.notification = new Notification("", 5000);

        this.authenticationCode = String.valueOf((int) (Math.random() * 1000000));

        addNewUserButton.addClickListener(buttonClickEvent -> onAddNewUserClick());
        activeUserButton.addClickListener(buttonClickEvent -> onActiveUserButtonClick());
        logInButton.addClickListener(buttonClickEvent -> getUI().ifPresent(ui -> ui.navigate(LoginView.ROUTE)));

        add(userNameTextField);
        add(emailTextField);
        add(verificationCodeTextField);

        add(userPasswordTextField1);
        add(userPasswordTextField2);

        add(addNewUserButton);
        add(activeUserButton);
        add(logInButton);

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    private void onActiveUserButtonClick() {
        if (newDebtorUser.getAuthenticationCode().equals(authenticationCode)) {
            Integer activeStatus = 1;
            newDebtorUser.setActive(activeStatus);
            debtorUserRepo.save(newDebtorUser);

            logInButton.setVisible(true);

            notification.setText("CORRECT CODE!");
            notification.open();

        } else {
            notification.setText("WRONG CODE!");
            notification.open();
        }
    }

    private void onAddNewUserClick() {
        newDebtorUser = new DebtorUser(
                userNameTextField.getValue(),
                passwordEncoder.encode(userPasswordTextField2.getValue()),
                "USER",
                "",
                emailTextField.getValue(),
                0,
                authenticationCode);

        debtorUserRepo.save(newDebtorUser);

        sendEmailWithTheCode(emailTextField.getValue(), newDebtorUser.getAuthenticationCode());

        userNameTextField.setVisible(false);
        emailTextField.setVisible(false);
        verificationCodeTextField.setVisible(false);
        userPasswordTextField1.setVisible(false);
        userPasswordTextField2.setVisible(false);
        addNewUserButton.setVisible(false);


        verificationCodeTextField.setVisible(true);
        activeUserButton.setVisible(true);

        notification.setText("Your account isn't active! Copy and paste code from your email!");
        notification.open();
    }

    private void sendEmailWithTheCode(String email, String code) {
        notificationService.sendNotification(email, code);
    }
}
