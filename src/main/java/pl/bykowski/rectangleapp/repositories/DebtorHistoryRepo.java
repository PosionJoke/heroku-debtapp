package pl.bykowski.rectangleapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.bykowski.rectangleapp.model.DebtorHistory;

import java.util.List;

@Repository
public interface DebtorHistoryRepo extends CrudRepository<DebtorHistory, Long> {
    List<DebtorHistory> findByUserName(String userName);
}
