package pl.bykowski.rectangleapp.services;

import org.apache.log4j.Logger;
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
    private static final Logger logger = Logger.getLogger(DebtorHistoryService.class);

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

        logger.debug("Save DebtorHistory\nid : " + debtorHistory.getId() +
                "\n based on DebtorDetails id : " + debtorDetails.getId());
        debtorHistoryRepo.save(debtorHistory);
    }

    public List<DebtorHistory> findByUserName(String name) {
        return debtorHistoryRepo.findByUserName(name);
    }
}
