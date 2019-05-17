package pl.bykowski.rectangleapp;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebtorRepo extends CrudRepository<Debtor, Long> {
}
