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
import pl.bykowski.rectangleapp.model.dto.DebtorUserDTO;
import pl.bykowski.rectangleapp.model.dto.NewFriendDTO;
import pl.bykowski.rectangleapp.model.dto.UserDTO;
import pl.bykowski.rectangleapp.services.FriendService;
import pl.bykowski.rectangleapp.services.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Objects;

@Log4j
@Controller
public class UserController {

    private final UserService userService;
    private final FriendService friendService;

    public UserController(UserService userService, FriendService friendService) {
        this.userService = Objects.requireNonNull(userService, "userService must be not null");
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

            DebtorUser debtorUser = userService.findByName(userDTO.getName());
            debtorUser.setActive(1);
            userService.save(debtorUser);

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

    @GetMapping("/friend-list")
    public ModelAndView returnFriendList(Principal principal){
        DebtorUser debtorUser = userService.findByName(principal.getName());
        return new ModelAndView("friend-list")
                .addObject("debtorUserFriendsSet", debtorUser.getFriendsList())
                .addObject("debtorUserFriendOfSet", debtorUser.getFriendOf())
                .addObject("debtorUserInvitesSet", debtorUser.getInvitesToFriendListSet());
    }

    @GetMapping("/create-new-friend")
    public ModelAndView returnCreateFriendForm(){
        return new ModelAndView("add-to-invite-list")
                .addObject("newFriendDTO", new NewFriendDTO());
    }

    @PostMapping("/add-to-invite-list")
    public ModelAndView saveNewFriend(@Valid @ModelAttribute NewFriendDTO newFriendDTO, BindingResult bindingResult,
                                      Principal principal){

        if (bindingResult.hasErrors()) {
            return new ModelAndView("add-to-invite-list", bindingResult.getModel())
                    .addObject("newFriendDTO", new NewFriendDTO());
        }

        friendService.addToInvitedList(principal.getName(), newFriendDTO.getName());
        DebtorUser debtorUser = userService.findByName(principal.getName());

        return new ModelAndView("friend-list")
                .addObject("debtorUserFriendsSet", debtorUser.getFriendsList())
                .addObject("debtorUserInvitesSet", debtorUser.getInvitesToFriendListSet());
    }

    @GetMapping("/add-to-friend-list")
    public ModelAndView addToFriendList(Principal principal, @RequestParam Long id){
        friendService.addToFriendList(principal.getName(), id);
        DebtorUser debtorUser = userService.findByName(principal.getName());

        return new ModelAndView("friend-list")
                .addObject("debtorUserFriendsSet", debtorUser.getFriendsList())
                .addObject("debtorUserInvitesSet", debtorUser.getInvitesToFriendListSet());
    }

    @GetMapping("/delete-friend")
    public ModelAndView deleteFriend(Principal principal, @RequestParam Long id){
        DebtorUser debtorUser = userService.findByName(principal.getName());

        friendService.deleteFriend(debtorUser.getId(), id);

        return new ModelAndView("friend-list")
                .addObject("debtorUserFriendsSet", debtorUser.getFriendsList())
                .addObject("debtorUserInvitesSet", debtorUser.getInvitesToFriendListSet());
    }
}
