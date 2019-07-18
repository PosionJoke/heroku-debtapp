package pl.bykowski.rectangleapp.Repositories.RepoInterfaces;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.bykowski.rectangleapp.Repositories.RepoStruct.Debtor;
import pl.bykowski.rectangleapp.Repositories.RepoStruct.DebtorDetails;

import java.util.List;

@Repository
public interface DebtorRepo extends CrudRepository<Debtor, Long> {
    List<Debtor> findByName(String name);
    List<DebtorDetails> findByNameAndId(String name, Long ID);
}
