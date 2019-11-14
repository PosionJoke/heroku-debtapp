package pl.bykowski.rectangleapp.services;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.model.dto.DebtorDetailsDTO;
import pl.bykowski.rectangleapp.repositories.DebtorRepo;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
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

    public Debtor findById(Long id) {
        Optional<Debtor> debtorOpt = debtorRepo.findById(id);
        return debtorOpt.orElseThrow(() -> new EntityNotFoundException(
                String.format("Unable to get Debtor id : [%s]", id)));
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
        Debtor changedDebtor = findById(debtorId);
        BigDecimal newDebt = debtValue.add(changedDebtor.getTotalDebt());
        changedDebtor.setTotalDebt(newDebt);
        saveDebtor(changedDebtor);

        return changedDebtor.getTotalDebt();
    }

    @Transactional
    public void updateTotalDebtAndUpdateDebtorDetailsDebt(DebtorDetailsDTO debtorDetailsDTO, Long debtorDetailsId) {
        deleteDebtIfIsUnderZero(debtorDetailsDTO);

        debtorDetailsService.updateDebtorDetailsDebt(debtorDetailsId, debtorDetailsDTO.getDebt());

        DebtorDetails debtorDetails = debtorDetailsService.findById(debtorDetailsId);

        updateTotalDebt(debtorDetails.getDebtor().getId(), debtorDetailsDTO.getDebt());
    }

    private void deleteDebtIfIsUnderZero(DebtorDetailsDTO debtorDetailsDTO) {
        BigDecimal debtAfterUpdate = debtorDetailsDTO.getDebt();
        BigDecimal debtBeforeUpdate = debtorDetailsService.findById(debtorDetailsDTO.getId()).getDebt();

        if (debtBeforeUpdate.min(debtAfterUpdate).floatValue() <= 0) {
            deleteDebtorDetailsUpdateTotalDebtMakeNewDebtorHistory(debtorDetailsDTO.getId());
        }
    }

    public void deleteDebtorDetailsUpdateTotalDebtMakeNewDebtorHistory(Long id) {
        DebtorDetails debtorDetailsOpt = debtorDetailsService.findById(id);

        updateTotalDebt(debtorDetailsOpt.getDebtor().getId(), debtorDetailsOpt.getDebt().multiply(new BigDecimal(-1)));
        debtorHistoryService.saveEntityDebtorHistory(debtorDetailsOpt);

        log.debug(String.format("Delete DebtorDetails id : [%s]", id));
        debtorDetailsService.deleteById(id);
    }

    public void addNewDebtor(DebtorDetailsDTO debtorDetailsDTO, String userName) {
        String actualUserName = userService.findUserName();
        if (isThisNameFree(debtorDetailsDTO.getName(), actualUserName)) {

            Debtor debtor = Debtor.builder()
                    .name(debtorDetailsDTO.getName())
                    .totalDebt(debtorDetailsDTO.getDebt())
                    .userName(userName)
                    .build();

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

    public Debtor findDebtorByName(String name) {
        Optional<Debtor> debtorOpt = debtorRepo.findByName(name);
        return debtorOpt.orElseThrow(() -> new EntityNotFoundException(
                String.format("Unable to get Debtor name : [%s]", name)));
    }
}
