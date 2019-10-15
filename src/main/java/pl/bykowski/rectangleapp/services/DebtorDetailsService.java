package pl.bykowski.rectangleapp.services;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.repositories.DebtorDetailsRepo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j
@Service
public class DebtorDetailsService {

    private final DebtorDetailsRepo debtorDetailsRepo;
    private final DebtorHistoryService debtorHistoryService;

    public DebtorDetailsService(DebtorDetailsRepo debtorDetailsRepo, DebtorHistoryService debtorHistoryService) {
        this.debtorDetailsRepo = Objects.requireNonNull(debtorDetailsRepo, "debtorDetailsRepo must be not null");
        this.debtorHistoryService = Objects.requireNonNull(debtorHistoryService, "debtorHistoryService must be not null");
    }

    private void saveDebtorDetails(DebtorDetails debtorDetails) {
        log.debug(String.format("Save to debtorDetailsRepo id : [%s], name : [%s]", debtorDetails.getId(),
                debtorDetails.getName()));
        debtorDetailsRepo.save(debtorDetails);
    }

    DebtorDetails addNewDebtorDetails(String debtorName, BigDecimal debtValue, String reasonForTheDebt, String userName, Debtor debtor) {
        DebtorDetails debtorDetails = new DebtorDetails();
        debtorDetails.setName(debtorName);
        debtorDetails.setDebt(debtValue);
        debtorDetails.setDate(LocalDate.now());
        debtorDetails.setReasonForTheDebt(reasonForTheDebt);
        debtorDetails.setUserName(userName);
        debtorDetails.setDebtor(debtor);

        saveDebtorDetails(debtorDetails);
        return debtorDetails;
    }

    @Transactional
    public void updateDebtorDetailsDebt(Long debtID, BigDecimal debtValue) {
        Optional<DebtorDetails> debtorDetails = debtorDetailsRepo.findById(debtID);
        debtorDetails.ifPresent(debtorD -> isThisDebtUnderZero(debtorD, debtValue));
    }

    Optional<DebtorDetails> findById(Long id) {
        return debtorDetailsRepo.findById(id);
    }

    void deleteById(Long id) {
        debtorDetailsRepo.deleteById(id);
    }

    private void isThisDebtUnderZero(DebtorDetails debtorDetails, BigDecimal debtValue) {
        BigDecimal newDebt = debtorDetails.getDebt().add(debtValue);
        if (newDebt.compareTo(new BigDecimal(0)) <= 0) {
            debtorDetails.setDebt(new BigDecimal(0));
            log.debug(String.format("Delete DebtorDetails, id : [%s], Debt should be equals 0 : [%s]",
                    debtorDetails.getId(), debtorDetails.getDebt()));
            deleteDebtById(debtorDetails.getId());
        } else {
            log.debug(String.format("Update totalDebt id : [%s], " +
                    "actual debt : [%s], added value : [%s], new debt = [%s]",
                    debtorDetails.getId(), debtorDetails.getDebt(), debtValue, newDebt));

            debtorDetails.setDebt(newDebt);

            debtorDetailsRepo.save(debtorDetails);
        }
    }

    @Transactional
    public void deleteDebtById(Long debtorID) {
        Optional<DebtorDetails> debtorDetailsCopyOptional = debtorDetailsRepo.findById(debtorID);
        debtorDetailsCopyOptional.ifPresent(debtorDetails -> {
            debtorHistoryService.saveEntityDebtorHistory(debtorDetails);
            log.debug(String.format("Delete DebtorDetails id : [%s]", debtorDetails.getId()));

            debtorDetailsRepo.delete(debtorDetails);
        });
    }

    public List<DebtorDetails> findByUserName(String name) {
        return debtorDetailsRepo.findByUserName(name);
    }
}
