package pl.bykowski.rectangleapp.repositories.repo_interfaces;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.bykowski.rectangleapp.repositories.repo_struct.DebtorHistory;

import java.util.List;

@Repository
public interface DebtorHistoryRepo extends CrudRepository<DebtorHistory, Long> {
    List<DebtorHistory> findByNameAndId(String name, Long ID);
}
