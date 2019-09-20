package pl.bykowski.rectangleapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.bykowski.rectangleapp.model.DebtorUser;

import java.util.List;

@Repository
public interface DebtorUserRepo extends CrudRepository<DebtorUser, Long> {
    DebtorUser findByName(String name);
}
