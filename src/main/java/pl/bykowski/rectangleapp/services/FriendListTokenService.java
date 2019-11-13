package pl.bykowski.rectangleapp.services;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import pl.bykowski.rectangleapp.model.FriendListToken;
import pl.bykowski.rectangleapp.repositories.FriendListTokenRepo;

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

    public Optional<FriendListToken> findByUserName(String name) {
        return friendListTokenRepo.findByUserName(name);
    }

    public Optional<FriendListToken> findByUserId(Long id) {
        return friendListTokenRepo.findByUserId(id);
    }
}
