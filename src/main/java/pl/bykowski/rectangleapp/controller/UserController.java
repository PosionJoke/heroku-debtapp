package pl.bykowski.rectangleapp.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import pl.bykowski.rectangleapp.model.DebtorUser;
import pl.bykowski.rectangleapp.model.DebtorUserDOT;
import pl.bykowski.rectangleapp.repositories.DebtorUserRepo;

@RestController
public class UserController {

    private DebtorUserRepo debtorUserRepo;
    private PasswordEncoder passwordEncoder;

    public UserController(DebtorUserRepo debtorUserRepo, PasswordEncoder passwordEncoder) {
        this.debtorUserRepo = debtorUserRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/create-new-user")
    public RedirectView createNewUser(@ModelAttribute DebtorUserDOT debtorUserDOT){

        DebtorUser newDebtorUser = new DebtorUser(
                debtorUserDOT.getName(),
                passwordEncoder.encode(debtorUserDOT.getPassword2()),
                "USER",
                "",
                debtorUserDOT.getEmail(),
                1,
                "authenticationCode");

        debtorUserRepo.save(newDebtorUser);
        return new RedirectView("main-view");
    }

    @GetMapping("/create-new-user")
    public ModelAndView returnLoginForm(){
        return new ModelAndView("create-new-user")
                .addObject("user", new DebtorUserDOT());
    }

}
