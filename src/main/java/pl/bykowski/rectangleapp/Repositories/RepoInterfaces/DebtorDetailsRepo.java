package pl.bykowski.rectangleapp.Repositories.RepoInterfaces;

import org.springframework.data.repository.CrudRepository;
import pl.bykowski.rectangleapp.Repositories.RepoStruct.DebtorDetails;

import java.util.List;

public interface DebtorDetailsRepo extends CrudRepository<DebtorDetails, Long> {
    List<DebtorDetails> findByName(String name);
    List<DebtorDetails> findByNameAndId(String name, Long ID);
}
