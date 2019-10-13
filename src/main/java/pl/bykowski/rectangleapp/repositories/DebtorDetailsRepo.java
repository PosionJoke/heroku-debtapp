package pl.bykowski.rectangleapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.bykowski.rectangleapp.model.DebtorDetails;

import java.util.List;
import java.util.Optional;

@Repository
public interface DebtorDetailsRepo extends CrudRepository<DebtorDetails, Long> {
    List<DebtorDetails> findByUserName(String userName);
    Optional<DebtorDetails> findByName(String name);
}
