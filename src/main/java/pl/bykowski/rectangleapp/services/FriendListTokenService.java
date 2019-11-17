package pl.bykowski.rectangleapp.services;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import pl.bykowski.rectangleapp.model.FriendListToken;
import pl.bykowski.rectangleapp.repositories.FriendListTokenRepo;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;
import java.util.Optional;

@Log4j
@Service
public class FriendListTokenService {

    private final FriendListTokenRepo friendListTokenRepo;

    public FriendListTokenService(FriendListTokenRepo friendListTokenRepo) {
        this.friendListTokenRepo = Objects.requireNonNull(friendListTokenRepo,
                "friendListTokenRepo must be not null");
    }

    public FriendListToken findByUserName(String name) {
        Optional<FriendListToken> token = friendListTokenRepo.findByUserName(name);
        return token.orElseThrow(() -> new EntityNotFoundException(
                String.format("Unable to get FriendListToken name : [%s]", name)));
    }

    FriendListToken findByUserId(Long id) {
        Optional<FriendListToken> token = friendListTokenRepo.findByUserId(id);
        return token.orElseThrow(() -> new EntityNotFoundException(
                String.format("Unable to get FriendListToken id : [%s]", id)));
    }

    public void save(FriendListToken friendListToken){
        friendListTokenRepo.save(friendListToken);
    }
}
