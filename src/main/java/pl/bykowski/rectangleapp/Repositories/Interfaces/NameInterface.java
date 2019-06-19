package pl.bykowski.rectangleapp.Repositories.Interfaces;

import pl.bykowski.rectangleapp.Repositories.DebtorRepository.Debtor;
import pl.bykowski.rectangleapp.Repositories.DeptorMoreInfoRepository.DebtorDetails;

import java.util.List;

public interface NameInterface<T> extends IDInterface {
    List<T> findByName(String name);
    List<DebtorDetails> findByNameAndId(String name, Long ID);
}
