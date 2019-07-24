package pl.bykowski.rectangleapp.repositories.repo_interfaces;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.bykowski.rectangleapp.repositories.repo_struct.Debtor;
import pl.bykowski.rectangleapp.repositories.repo_struct.DebtorDetails;

import java.util.List;

@Repository
public interface DebtorRepo extends CrudRepository<Debtor, Long> {
    List<Debtor> findByName(String name);
    List<DebtorDetails> findByNameAndId(String name, Long ID);
}
