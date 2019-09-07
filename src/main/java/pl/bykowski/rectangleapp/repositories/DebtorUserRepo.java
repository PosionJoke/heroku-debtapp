package pl.bykowski.rectangleapp.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.bykowski.rectangleapp.model.DebtorUser;

import java.util.List;


public interface DebtorUserRepo extends CrudRepository<DebtorUser, Long> {
    List<DebtorUser> findByName(String name);
}
