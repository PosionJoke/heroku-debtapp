package pl.bykowski.rectangleapp.repositories;

import pl.bykowski.rectangleapp.model.DebtorHistory;

import java.util.List;
import java.util.Optional;

public class DebtorHistoryRepoImp implements DebtorHistoryRepo {
    @Override
    public List<DebtorHistory> findByUserName(String userName) {
        return null;
    }

    @Override
    public <S extends DebtorHistory> S save(S s) {
        return null;
    }

    @Override
    public <S extends DebtorHistory> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<DebtorHistory> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<DebtorHistory> findAll() {
        return null;
    }

    @Override
    public Iterable<DebtorHistory> findAllById(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(DebtorHistory debtorHistory) {

    }

    @Override
    public void deleteAll(Iterable<? extends DebtorHistory> iterable) {

    }

    @Override
    public void deleteAll() {

    }
}
