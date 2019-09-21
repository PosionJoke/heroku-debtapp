package pl.bykowski.rectangleapp.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import pl.bykowski.rectangleapp.model.DebtorUser;
import pl.bykowski.rectangleapp.model.dto.DebtorUserDTO;
import pl.bykowski.rectangleapp.model.dto.UserDTO;
import pl.bykowski.rectangleapp.repositories.DebtorUserRepo;
import pl.bykowski.rectangleapp.services.NotificationService;
import pl.bykowski.rectangleapp.services.UserService;

import java.util.Objects;

@RestController
public class UserController {

    private final DebtorUserRepo debtorUserRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final NotificationService notificationService;

    public UserController(DebtorUserRepo debtorUserRepo, PasswordEncoder passwordEncoder, UserService userService,
                          NotificationService notificationService) {
        this.debtorUserRepo = Objects.requireNonNull(debtorUserRepo, "debtorUserRepo must be not null");
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder, "passwordEncoder must be not null");
        this.userService = Objects.requireNonNull(userService, "userService must be not null");
        this.notificationService = Objects.requireNonNull(notificationService, "notificationService must be not null");
    }

    @PostMapping("/create-new-user")
    public ModelAndView createNewUser(@ModelAttribute DebtorUserDTO debtorUserDTO){

        //TODO HELLO ADRIAN, there is some code to test. We need to check passwords and return feedback to user about that
        if(userService.checkPassword(debtorUserDTO.getPassword1(), debtorUserDTO.getPassword2()) == false){
            String errorMsg = "Passwords isn't equals";
            return new ModelAndView("create-new-user")
                    .addObject("user", new DebtorUserDTO())
                    .addObject("errorMsg", errorMsg);
        }

        String authenticationCode = userService.generateNewAuthenticationCode();
        //ACTIVE 0 - no active account / 1 - active account
        DebtorUser newDebtorUser = new DebtorUser(
                debtorUserDTO.getName(),
                passwordEncoder.encode(debtorUserDTO.getPassword2()),
                "USER",
                "",
                debtorUserDTO.getEmail(),
                0,
                authenticationCode);

        notificationService.sendNotification(newDebtorUser.getEmail(), authenticationCode);
        UserDTO userDTO = new UserDTO();
        userDTO.setAuthenticationCode(authenticationCode);
        userDTO.setName(newDebtorUser.getName());

        debtorUserRepo.save(newDebtorUser);
        return new ModelAndView("create-new-user-authentication")
                .addObject("userDTO", userDTO);
    }

    @PostMapping("/create-new-user-authentication")
    public ModelAndView activeAccount(@ModelAttribute DebtorUserDTO userDTO){


        if(userService.checkAuthenticationCode(userDTO.getAuthenticationCode(), userDTO.getAuthenticationCodeInput())){
            DebtorUser debtorUser = userService.findByName(userDTO.getName());
            debtorUser.setActive(1);
            userService.save(debtorUser);

            return new  ModelAndView("default-view");
        }
//        String errorMsg = ""
        return new ModelAndView("create-new-user-authentication")
                .addObject("userDTO", userDTO);
//                .addObject("errorMsg", )
    }

    @GetMapping("/create-new-user")
    public ModelAndView returnLoginForm(){
        return new ModelAndView("create-new-user")
                .addObject("user", new DebtorUserDTO());
    }



}
