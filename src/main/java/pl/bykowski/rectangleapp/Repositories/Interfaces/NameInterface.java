package pl.bykowski.rectangleapp.Repositories.Interfaces;

import pl.bykowski.rectangleapp.Repositories.DebtorRepository.Debtor;

import java.util.List;

public interface NameInterface<T> extends IDInterface {
    List<T> findByName(String name);
}
