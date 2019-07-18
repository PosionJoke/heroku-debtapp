package pl.bykowski.rectangleapp.Repositories.RepoInterfaces;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.bykowski.rectangleapp.Repositories.RepoStruct.DebtorDetails;
import pl.bykowski.rectangleapp.Repositories.RepoStruct.DebtorHistory;

import java.util.List;

@Repository
public interface DebtorHistoryRepo extends CrudRepository<DebtorHistory, Long> {
    List<DebtorHistory> findByName(String name);
    List<DebtorDetails> findByNameAndId(String name, Long ID);
}
