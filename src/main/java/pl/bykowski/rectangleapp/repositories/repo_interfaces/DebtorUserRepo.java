package pl.bykowski.rectangleapp.repositories.repo_interfaces;

import org.springframework.data.repository.CrudRepository;
import pl.bykowski.rectangleapp.repositories.repo_struct.DebtorUser;

import java.util.List;


public interface DebtorUserRepo extends CrudRepository<DebtorUser, Long> {
    List<DebtorUser> findByName(String name);
}
