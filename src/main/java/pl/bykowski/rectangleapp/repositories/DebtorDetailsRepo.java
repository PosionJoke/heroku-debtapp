package pl.bykowski.rectangleapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.bykowski.rectangleapp.model.DebtorDetails;

import java.util.List;

@Repository
public interface DebtorDetailsRepo extends CrudRepository<DebtorDetails, Long> {
    DebtorDetails findByNameAndId(String name, Long ID);
    List<DebtorDetails> findByUserName(String userName);
    DebtorDetails findByName(String name);
}
