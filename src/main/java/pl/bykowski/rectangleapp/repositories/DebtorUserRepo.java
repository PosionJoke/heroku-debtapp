package pl.bykowski.rectangleapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.bykowski.rectangleapp.model.DebtorUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface DebtorUserRepo extends CrudRepository<DebtorUser, Long> {
    Optional<DebtorUser> findByName(String name);
}
