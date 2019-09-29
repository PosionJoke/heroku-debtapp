package pl.bykowski.rectangleapp.services;

import org.apache.log4j.Logger;
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

@Service
public class DebtorDetailsService {

    private static final Logger logger = Logger.getLogger(DebtorDetailsService.class);
    private final DebtorDetailsRepo debtorDetailsRepo;
    private final DebtorHistoryService debtorHistoryService;

    public DebtorDetailsService(DebtorDetailsRepo debtorDetailsRepo, DebtorHistoryService debtorHistoryService) {
        this.debtorDetailsRepo = Objects.requireNonNull(debtorDetailsRepo, "debtorDetailsRepo must be not null");
        this.debtorHistoryService = Objects.requireNonNull(debtorHistoryService, "debtorHistoryService must be not null");
    }

    private void saveDebtorDetails(DebtorDetails debtorDetails) {
        logger.debug("Save to debtorDetailsRepo\nid : " + debtorDetails.getId() + "\n" + "name : " + debtorDetails.getName());
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
            logger.debug("Delete DebtorDetails\nid : " + debtorDetails.getId() + "\nDebt should be equals 0 : " + debtorDetails.getDebt());
            deleteDebtById(debtorDetails.getId());
        } else {
            logger.debug("Update totalDebt\nid : " + debtorDetails.getId() +
                    "\nactual debt : " + debtorDetails.getDebt() +
                    "\nadded value : " + debtValue +
                    "\nnew debt" + newDebt);
            debtorDetails.setDebt(newDebt);
            debtorDetailsRepo.save(debtorDetails);
        }
    }

    @Transactional
    public void deleteDebtById(Long debtorID) {
        Optional<DebtorDetails> debtorDetailsCopyOptional = debtorDetailsRepo.findById(debtorID);
        debtorDetailsCopyOptional.ifPresent(debtorDetails -> {
            debtorHistoryService.saveEntityDebtorHistory(debtorDetails);
            logger.debug("Delete DebtorDetails\nid : " + debtorDetails.getId());
            debtorDetailsRepo.delete(debtorDetails);
        });
    }

    public List<DebtorDetails> findByUserName(String name) {
        return debtorDetailsRepo.findByUserName(name);
    }
}
