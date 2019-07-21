package pl.bykowski.rectangleapp.Repositories.RepoInterfaces;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.bykowski.rectangleapp.Repositories.RepoStruct.DebtorDetails;

import java.util.List;

@Repository
public interface DebtorDetailsRepo extends CrudRepository<DebtorDetails, Long> {
    List<DebtorDetails> findByName(String name);
    List<DebtorDetails> findByNameAndId(String name, Long ID);
}
