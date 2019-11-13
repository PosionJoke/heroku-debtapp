package pl.bykowski.rectangleapp.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.bykowski.rectangleapp.model.DebtorUser;
import pl.bykowski.rectangleapp.model.FriendListToken;
import pl.bykowski.rectangleapp.model.dto.DebtorUserDTO;
import pl.bykowski.rectangleapp.model.dto.UserDTO;
import pl.bykowski.rectangleapp.repositories.FriendListTokenRepo;
import pl.bykowski.rectangleapp.services.FriendService;
import pl.bykowski.rectangleapp.services.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Log4j
@Controller
public class UserController {

    private final UserService userService;
    private final FriendService friendService;
    //TODO this should be service
    private final FriendListTokenRepo friendListTokenRepo;

    public UserController(UserService userService, FriendListTokenRepo invitesToFriendListRepo,
                          FriendService friendService) {
        this.userService = Objects.requireNonNull(userService, "userService must be not null");
        this.friendListTokenRepo = Objects.requireNonNull(invitesToFriendListRepo, "friendListTokenRepo must be not null");
        this.friendService = Objects.requireNonNull(friendService, "friendService must be not null");
    }

    @PostMapping("/create-new-user")
    public ModelAndView createNewUser(@Valid @ModelAttribute DebtorUserDTO debtorUserDTO,
                                      BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("create-new-user", bindingResult.getModel());
        }

        UserDTO userDTO = userService.makeNewUser(debtorUserDTO);

        return new ModelAndView("create-new-user-authentication")
                .addObject("userDTO", userDTO);
    }

    @PostMapping("/create-new-user-authentication")
    public ModelAndView activeAccount(@Valid @ModelAttribute UserDTO userDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("create-new-user-authentication", bindingResult.getModel());
        }

        if (userService.checkAuthenticationCode(
                userDTO.getAuthenticationCode(), userDTO.getAuthenticationCodeInput())) {

            Optional<DebtorUser> debtorUser = userService.findByName(userDTO.getName());

            if (debtorUser.isPresent()) {
                debtorUser.get().setActive(1);
                userService.save(debtorUser.get());
            } else {
                log.error(String.format("User [%s] does not exist", userDTO.getName()));
            }

            return new ModelAndView("default-view");
        }

        return new ModelAndView("create-new-user-authentication")
                .addObject("userDTO", userDTO);
    }

    @GetMapping("/create-new-user")
    public ModelAndView returnLoginForm() {
        return new ModelAndView("create-new-user")
                .addObject("debtorUserDTO", new DebtorUserDTO());
    }
//---------------------------------------------------------------------------------------------------------------------
    @GetMapping("/friend-list")
    public ModelAndView returnFriendList(Principal principal){
        Optional<DebtorUser> debtorUser = userService.findByName(principal.getName());
        return new ModelAndView("friend-list")
                .addObject("debtorUserFriendsSet", debtorUser.get().getFriendsList())
                .addObject("debtorUserFriendOfSet", debtorUser.get().getFriendOf())
                .addObject("debtorUserInvitesSet", debtorUser.get().getInvitesToFriendListSet());
    }

    @GetMapping("/create-new-friend")
    public ModelAndView returnCreateFriendForm(){
        return new ModelAndView("add-to-invite-list")
                .addObject("debtorUserDTO", new DebtorUserDTO());
    }

    @PostMapping("/add-to-invite-list")
    public ModelAndView saveNewFriend(@ModelAttribute DebtorUserDTO debtorUserDTO, Principal principal){
        friendService.addToInvitedList(principal.getName(), debtorUserDTO.getName());
        DebtorUser debtorUser = userService.findByName(principal.getName()).orElseGet(DebtorUser::new);

        return new ModelAndView("friend-list")
                .addObject("debtorUserFriendsSet", debtorUser.getFriendsList())
                .addObject("debtorUserInvitesSet", debtorUser.getInvitesToFriendListSet());
    }

    @GetMapping("/add-to-friend-list")
    public ModelAndView addToFriendList(Principal principal, @RequestParam Long id){
        friendService.addToFriendList(principal.getName(), id);
        DebtorUser debtorUser = userService.findByName(principal.getName()).orElseGet(DebtorUser::new);

        return new ModelAndView("friend-list")
                .addObject("debtorUserFriendsSet", debtorUser.getFriendsList())
                .addObject("debtorUserInvitesSet", debtorUser.getInvitesToFriendListSet());
    }
}
