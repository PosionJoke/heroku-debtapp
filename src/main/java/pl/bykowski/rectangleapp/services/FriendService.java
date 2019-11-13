package pl.bykowski.rectangleapp.services;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import pl.bykowski.rectangleapp.model.DebtorUser;
import pl.bykowski.rectangleapp.model.FriendListToken;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Log4j
@Service
public class FriendService {

    private final FriendListTokenService friendListTokenService;
    private final UserService userService;

    public FriendService(FriendListTokenService friendListTokenService, UserService userService) {
        this.friendListTokenService = Objects.requireNonNull(friendListTokenService,
                "friendListTokenService must be not null");
        this.userService = Objects.requireNonNull(userService,
                "userService must be not null");
    }
    //TODO Find better way to deal with Optional
    public void addToInvitedList(String actualUserName, String newFriendName){

        Optional<DebtorUser> actualUserOpt = userService.findByName(actualUserName);
        if(!actualUserOpt.isPresent()){
            log.warn(String.format("Can't find DebtorUser with user name : [%s]", actualUserName));
        }
        DebtorUser actualUser = actualUserOpt.orElseGet(DebtorUser::new);

        Optional<DebtorUser> newFriendOpt = userService.findByName(newFriendName);
        if(!newFriendOpt.isPresent()){
            log.warn(String.format("Can't find DebtorUser with user name : [%s]", newFriendName));
        }

        Optional<FriendListToken> friendListTokenOpt = friendListTokenService.findByUserName(actualUser.getName());
        if(!friendListTokenOpt.isPresent()){
            log.warn(String.format("Can't find FriendListToken with user name : [%s]", actualUser.getName()));
        }

        FriendListToken friendListToken = friendListTokenOpt.orElseGet(FriendListToken::new);

        newFriendOpt.ifPresent(newFriend -> {
            Set<FriendListToken> invitesToFriendList = newFriend.getInvitesToFriendListSet();
            invitesToFriendList.add(friendListToken);
            newFriend.setInvitesToFriendListSet(invitesToFriendList);
            userService.save(newFriend);
        });
    }
}
