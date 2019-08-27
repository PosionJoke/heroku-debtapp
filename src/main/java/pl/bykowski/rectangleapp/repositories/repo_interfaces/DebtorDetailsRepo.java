package pl.bykowski.rectangleapp.repositories.repo_interfaces;

import org.springframework.data.repository.CrudRepository;
import pl.bykowski.rectangleapp.repositories.repo_struct.DebtorDetails;

import java.util.List;

public interface DebtorDetailsRepo extends CrudRepository<DebtorDetails, Long> {
    DebtorDetails findByName(String name);

    DebtorDetails findByNameAndId(String name, Long ID);
    List<DebtorDetails> findByUserName(String userName);
}
