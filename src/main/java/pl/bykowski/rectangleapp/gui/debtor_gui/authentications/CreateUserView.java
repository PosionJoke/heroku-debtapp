package pl.bykowski.rectangleapp.gui.debtor_gui.authentications;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.bykowski.rectangleapp.repositories.repo_interfaces.DebtorUserRepo;
import pl.bykowski.rectangleapp.repositories.repo_struct.DebtorUser;
import pl.bykowski.rectangleapp.services.DebtorUserService;

@Route(value = "create-user")
public class CreateUserView extends VerticalLayout {

    private DebtorUserRepo debtorUserRepo;
    private DebtorUserService debtorUserService;
    private PasswordEncoder passwordEncoder;

    private Button addNewUser;

    private TextField userNameTextField;
    private TextField emailTextField;
    private PasswordField userPasswordTextField1;
    private PasswordField userPasswordTextField2;

    public CreateUserView(DebtorUserRepo debtorUserRepo, DebtorUserService debtorUserService, PasswordEncoder passwordEncoder) {
        this.debtorUserRepo = debtorUserRepo;
        this.debtorUserService = debtorUserService;
        this.passwordEncoder = passwordEncoder;

        this.userNameTextField = new TextField("User Name");
        this.emailTextField = new TextField("Email");
        this.userPasswordTextField1 = new PasswordField("Password");
        this.userPasswordTextField2 = new PasswordField("Repeat password");

        this.addNewUser = new Button("Create Account!");

        addNewUser.addClickListener(buttonClickEvent -> onAddNewUserClick());

        add(userNameTextField);
        add(emailTextField);
        add(userPasswordTextField1);
        add(userPasswordTextField2);
        add(addNewUser);

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    private void onAddNewUserClick() {
        DebtorUser newDebtorUser = new DebtorUser(
                userNameTextField.getValue(),
                passwordEncoder.encode(userPasswordTextField2.getValue()),
                "USER",
                "",
                emailTextField.getValue());

        debtorUserRepo.save(newDebtorUser);
    }
}
