package pl.bykowski.rectangleapp.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.bykowski.rectangleapp.model.DebtorUser;
import pl.bykowski.rectangleapp.model.dto.DebtorUserDTO;
import pl.bykowski.rectangleapp.model.dto.UserDTO;
import pl.bykowski.rectangleapp.services.UserService;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;

@Controller
public class UserController {

    private static final Logger logger = Logger.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = Objects.requireNonNull(userService, "userService must be not null");
    }

    @PostMapping("/create-new-user")
    public ModelAndView createNewUser(@Valid @ModelAttribute DebtorUserDTO debtorUserDTO,
                                      BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ModelAndView("create-new-user", bindingResult.getModel());
        }
        UserDTO userDTO = userService.makeNewUser(debtorUserDTO);

        return new ModelAndView("create-new-user-authentication")
                .addObject("userDTO", userDTO);
    }

    @PostMapping("/create-new-user-authentication")
    public ModelAndView activeAccount(@ModelAttribute DebtorUserDTO debtorUserDTO){

        if(userService.checkAuthenticationCode(debtorUserDTO.getAuthenticationCode(), debtorUserDTO.getAuthenticationCodeInput())){
            Optional<DebtorUser> debtorUser = userService.findByName(debtorUserDTO.getName());
            debtorUser.ifPresentOrElse(debtorUser1 -> {
                        debtorUser1.setActive(1);
                        userService.save(debtorUser1);
                    },
                    () -> logger.debug("debtorUser must be present"));

            return new  ModelAndView("default-view");
        }

        return new ModelAndView("create-new-user-authentication")
                .addObject("userDTO", debtorUserDTO);
    }

    @GetMapping("/create-new-user")
    public ModelAndView returnLoginForm(){
        return new ModelAndView("create-new-user")
                .addObject("debtorUserDTO", new DebtorUserDTO());
    }



}
