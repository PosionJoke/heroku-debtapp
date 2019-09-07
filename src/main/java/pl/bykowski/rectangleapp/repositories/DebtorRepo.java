package pl.bykowski.rectangleapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.bykowski.rectangleapp.model.Debtor;

import java.util.List;

@Repository
public interface DebtorRepo extends CrudRepository<Debtor, Long> {
    Debtor findByName(String name);
    List<Debtor> findByUserName(String userName);

    List<Debtor> findAll();
}
