package pl.bykowski.rectangleapp.services;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.model.dto.DebtorDetailsDTO;
import pl.bykowski.rectangleapp.repositories.DebtorRepo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j
@Service
public class DebtorService {

    private final DebtorRepo debtorRepo;
    private final UserService userService;
    private final DebtorDetailsService debtorDetailsService;
    private final DebtorHistoryService debtorHistoryService;

    public DebtorService(DebtorRepo debtorRepo, UserService userService,
                         DebtorDetailsService debtorDetailsService, DebtorHistoryService debtorHistoryService) {
        this.debtorRepo = Objects.requireNonNull(debtorRepo, "debtorRepo must be not null");
        this.userService = Objects.requireNonNull(userService, "userService must be not null");
        this.debtorDetailsService = Objects.requireNonNull(debtorDetailsService,
                "debtorDetailsService must be not null");
        this.debtorHistoryService = Objects.requireNonNull(debtorHistoryService,
                "debtorHistoryService must be not null");
    }

    private void saveDebtor(Debtor debtor) {
        log.debug(String.format("Save Debtor id : [%s], name : [%s]", debtor.getId(), debtor.getName()));

        debtorRepo.save(debtor);
    }

    public Optional<Debtor> findById(Long id) {
        return debtorRepo.findById(id);
    }

    public List<Debtor> findByUserName(String name) {
        return debtorRepo.findByUserName(name);
    }

    private boolean isThisNameFree(String debtorName, String userName) {

        return findByUserName(userName)
                .stream()
                .noneMatch(debtor -> debtor.getName().equalsIgnoreCase(debtorName));
    }

    @Transactional
    public void updateTotalDebtAndMakeNewDebtorDetails(DebtorDetailsDTO debtorDetails, Debtor debtor, String userName) {
        debtorDetailsService.addNewDebtorDetails(debtorDetails, userName, debtor);

        updateTotalDebt(debtor.getId(), debtorDetails.getDebt());
    }

    public BigDecimal updateTotalDebt(Long debtorId, BigDecimal debtValue) {
        Optional<Debtor> changedDebtor = findById(debtorId);
        changedDebtor.ifPresent(debtor -> {
            BigDecimal newDebt = debtValue.add(debtor.getTotalDebt());
            debtor.setTotalDebt(newDebt);
            saveDebtor(debtor);
        });
        return changedDebtor.map(Debtor::getTotalDebt)
                .orElse(new BigDecimal(0));
    }

    @Transactional
    public void updateTotalDebtAndUpdateDebtorDetailsDebt(DebtorDetailsDTO debtorDetailsDTO, Long debtorDetailsId) {
        deleteDebtIfIsUnderZero(debtorDetailsDTO);

        debtorDetailsService.updateDebtorDetailsDebt(debtorDetailsId, debtorDetailsDTO.getDebt());

        Optional<DebtorDetails> debtorDetails = debtorDetailsService.findById(debtorDetailsId);

        if (debtorDetails.isPresent()) {
            updateTotalDebt(debtorDetails.get().getDebtor().getId(), debtorDetailsDTO.getDebt());
        } else {
            log.debug("debtorDetails must be present");
        }
    }

    private void deleteDebtIfIsUnderZero(DebtorDetailsDTO debtorDetailsDTO) {
        BigDecimal debtAfterUpdate = debtorDetailsDTO.getDebt();
        Optional<BigDecimal> debtBeforeUpdate = debtorDetailsService.findById(debtorDetailsDTO.getId()).map(DebtorDetails::getDebt);

        if (debtBeforeUpdate.isPresent()) {
            if (debtBeforeUpdate.get().min(debtAfterUpdate).floatValue() <= 0) {
                deleteDebtorDetailsUpdateTotalDebtMakeNewDebtorHistory(debtorDetailsDTO.getId());
            }
        } else {
            log.error(String.format("cant find debtor details with id : [%s]", debtorDetailsDTO.getId()));
        }
    }

    public void deleteDebtorDetailsUpdateTotalDebtMakeNewDebtorHistory(Long id) {
        Optional<DebtorDetails> debtorDetailsOpt = debtorDetailsService.findById(id);
        debtorDetailsOpt.ifPresent(debtorDetails -> {
            updateTotalDebt(debtorDetails.getDebtor().getId(), debtorDetails.getDebt().multiply(new BigDecimal(-1)));
            debtorHistoryService.saveEntityDebtorHistory(debtorDetails);
        });

        log.debug(String.format("Delete DebtorDetails id : [%s]", id));
        debtorDetailsService.deleteById(id);
    }

    public void addNewDebtor(DebtorDetailsDTO debtorDetailsDTO, String userName) {
        String actualUserName = userService.findUserName();
        if (isThisNameFree(debtorDetailsDTO.getName(), actualUserName)) {
            Debtor debtor = new Debtor();
            debtor.setName(debtorDetailsDTO.getName());
            debtor.setTotalDebt(debtorDetailsDTO.getDebt());
            debtor.setDateOfJoining(LocalDate.now());
            debtor.setUserName(userName);
            saveDebtor(debtor);

            debtorDetailsService.addNewDebtorDetails(debtorDetailsDTO, userName, debtor);
        }
    }

    public Optional<Debtor> returnDebtorWithBiggestDebt(String userName) {
        Optional<Debtor> debtorFound = findByUserName(userName)
                .stream()
                .max(Comparator.comparing(Debtor::getTotalDebt));

        if (debtorFound.isPresent()) {
            log.debug(String.format("Debtor with the biggest debt id : [%s], Debt Value : [%s]", debtorFound.get().getId(),
                    debtorFound.get().getTotalDebt()));
        } else {
            log.error("Can't find debtor");
        }

        return debtorFound;
    }

    public Optional<Debtor> findDebtorByName(String name) {
        return debtorRepo.findByName(name);
    }
}
