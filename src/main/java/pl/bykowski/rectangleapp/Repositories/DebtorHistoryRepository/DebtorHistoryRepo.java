package pl.bykowski.rectangleapp.Repositories.DebtorHistoryRepository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.bykowski.rectangleapp.Repositories.Interfaces.NameInterface;

@Repository
public interface DebtorHistoryRepo extends CrudRepository<DebtorHistory, Long>, NameInterface<DebtorHistory> {

}
