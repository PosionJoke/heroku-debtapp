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

    public void addToInvitedList(String actualUserName, String newFriendName) {

        DebtorUser actualUser = userService.findByName(actualUserName);

        DebtorUser newFriend = userService.findByName(newFriendName);

        Optional<FriendListToken> friendListTokenOpt = friendListTokenService.findByUserName(actualUser.getName());
        if (!friendListTokenOpt.isPresent()) {
            log.warn(String.format("Can't find FriendListToken with user name : [%s]", actualUser.getName()));
        }

        FriendListToken friendListToken = friendListTokenOpt.orElseGet(FriendListToken::new);

        Set<FriendListToken> invitesToFriendList = newFriend.getInvitesToFriendListSet();
        invitesToFriendList.add(friendListToken);
        newFriend.setInvitesToFriendListSet(invitesToFriendList);
        userService.save(newFriend);
    }

    public void addToFriendList(String actualUserName, Long newFriendId) {

        DebtorUser actualUser = userService.findByName(actualUserName);

        DebtorUser newFriend = userService.findById(newFriendId);
//        if (!newFriendOpt.isPresent()) {
//            log.warn(String.format("Can't find DebtorUser with user name : [%s]", newFriendId));
//        }
//        DebtorUser newFriend = newFriendOpt;

        Set<DebtorUser> friendList = actualUser.getFriendsList();
        friendList.add(newFriend);
        actualUser.setFriendsList(friendList);
        userService.save(actualUser);

        deleteFromInviteList(actualUser.getId(), newFriend.getId());
    }

    private void deleteFromInviteList(Long userId, Long userInListId){
        DebtorUser user = userService.findById(userId);
//        if (!userOpt.isPresent()) {
//            log.warn(String.format("Can't find DebtorUser with id : [%s]", userId));
//        }
//        DebtorUser user = userOpt.orElseGet(DebtorUser::new);

        Optional<FriendListToken> invitesToFriendListFromOpt = friendListTokenService.findByUserId(userInListId);
        if (!invitesToFriendListFromOpt.isPresent()) {
            log.warn(String.format("Can't find FriendListToken with user_id : [%s]", userInListId));
        }
        FriendListToken invitesToFriendListFrom = invitesToFriendListFromOpt.orElseGet(FriendListToken::new);

        Set<FriendListToken> invitesToFriendList = user.getInvitesToFriendListSet();
        invitesToFriendList.remove(invitesToFriendListFrom);
        user.setInvitesToFriendListSet(invitesToFriendList);

        userService.save(user);
    }
}
