package pl.bykowski.rectangleapp.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.bykowski.rectangleapp.model.InvitesToFriendList;

import java.util.List;

public interface InvitesToFriendListRepo extends CrudRepository<InvitesToFriendList, Long> {
    InvitesToFriendList findByUserId(Long id);
    List<InvitesToFriendList> findByUserName(String userName);
}
