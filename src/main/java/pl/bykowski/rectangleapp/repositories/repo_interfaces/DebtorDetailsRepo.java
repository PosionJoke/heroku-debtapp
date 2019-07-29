package pl.bykowski.rectangleapp.repositories.repo_interfaces;

import org.springframework.data.repository.CrudRepository;
import pl.bykowski.rectangleapp.repositories.repo_struct.DebtorDetails;

import java.time.LocalDate;
import java.util.List;

public interface DebtorDetailsRepo extends CrudRepository<DebtorDetails, Long> {
    List<DebtorDetails> findByName(String name);
    List<DebtorDetails> findByNameAndId(String name, Long ID);

    List<DebtorDetails> findByid(Long ID);

    List<DebtorDetails> findByDate(LocalDate localDate);
}
