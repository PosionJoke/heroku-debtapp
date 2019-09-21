package pl.bykowski.rectangleapp.services;

import org.springframework.stereotype.Service;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.model.DebtorHistory;
import pl.bykowski.rectangleapp.repositories.DebtorHistoryRepo;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class DebtorHistoryService {

    private final DebtorHistoryRepo debtorHistoryRepo;

    public DebtorHistoryService(DebtorHistoryRepo debtorHistoryRepo) {
        this.debtorHistoryRepo = Objects.requireNonNull(debtorHistoryRepo, "debtorHistoryRepo must be not null");
    }

    public void saveEntityDebtorHistory(DebtorDetails debtorDetails) {
        DebtorHistory debtorHistory = new DebtorHistory();
        debtorHistory.setDebt(debtorDetails.getDebt());
        debtorHistory.setName(debtorDetails.getName());
        debtorHistory.setReasonForTheDebt(debtorDetails.getReasonForTheDebt());
        debtorHistory.setUserName(debtorDetails.getUserName());

        long daysBetween = DAYS.between(debtorDetails.getDate(), LocalDate.now());
        debtorHistory.setTimeOfDebt(daysBetween);

        debtorHistoryRepo.save(debtorHistory);
    }

    public List<DebtorHistory> findByUserName(String name) {
        return debtorHistoryRepo.findByUserName(name);
    }
}
