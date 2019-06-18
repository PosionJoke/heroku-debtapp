package pl.bykowski.rectangleapp.Repositories.DebtorRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.bykowski.rectangleapp.Repositories.Interfaces.NameInterface;

import java.util.List;

@Repository
public interface DebtorRepo extends CrudRepository<Debtor, Long>, NameInterface<Debtor> {

}
