package pl.bykowski.rectangleapp.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.bykowski.rectangleapp.model.FriendListToken;

import java.util.Optional;

public interface FriendListTokenRepo extends CrudRepository<FriendListToken, Long> {
    Optional<FriendListToken> findByUserId(Long id);
    Optional<FriendListToken> findByUserName(String userName);
}
