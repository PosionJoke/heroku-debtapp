package pl.bykowski.rectangleapp.Repositories.DeptorMoreInfoRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.bykowski.rectangleapp.Repositories.DebtorRepository.Debtor;
import pl.bykowski.rectangleapp.Repositories.Interfaces.NameInterface;

import java.time.LocalDate;
import java.util.List;

public interface DebtorDetailsRepo extends CrudRepository<DebtorDetails, Long>, NameInterface<DebtorDetails> {

}
