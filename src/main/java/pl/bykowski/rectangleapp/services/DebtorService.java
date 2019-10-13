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
        this.debtorDetailsService = Objects.requireNonNull(debtorDetailsService, "debtorDetailsService must be not null");
        this.debtorHistoryService = Objects.requireNonNull(debtorHistoryService, "debtorHistoryService must be not null");
    }

    private void saveDebtor(Debtor debtor) {
        log.debug("Save Debtor\nid : " + debtor.getId() + "\nname : " + debtor.getName());
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
        debtorDetailsService.addNewDebtorDetails(debtorDetails.getName(), debtorDetails.getDebt(), debtorDetails.getReasonForTheDebt(), userName, debtor);
        updateTotalDebt(debtor.getId(), debtorDetails.getDebt(), userName);
    }

    public BigDecimal updateTotalDebt(Long debtorId, BigDecimal debtValue, String userName) {
        Optional<Debtor> changedDebtor = findById(debtorId);
        changedDebtor.ifPresent(debtor -> {
            BigDecimal newDebt = debtValue.add(debtor.getTotalDebt());
            debtor.setTotalDebt(newDebt);
            //todo check is this setUsername is necessary
            debtor.setUserName(userName);
            saveDebtor(debtor);
        });
        return changedDebtor.map(Debtor::getTotalDebt)
                .orElse(new BigDecimal(0));
    }

    @Transactional
    public void updateTotalDebtAndUpdateDebtorDetailsDebt(DebtorDetailsDTO debtorDetailsDTO, Long debtorDetailsId) {
        debtorDetailsService.updateDebtorDetailsDebt(debtorDetailsId, debtorDetailsDTO.getDebt());
        Optional<DebtorDetails> debtorDetails = debtorDetailsService.findById(debtorDetailsId);
        debtorDetails.ifPresentOrElse(debtorDetails1 ->
                        updateTotalDebt(debtorDetails1.getDebtor().getId(), debtorDetailsDTO.getDebt(), debtorDetails1.getUserName())
                ,
                () -> log.debug("@PostMapping /debtor-details-save \ndebtorDetails must be present")
        );
    }

    public void deleteDebtorDetailsUpdateTotalDebtMakeNewDebtorHistory(Long id, String userName) {
        Optional<DebtorDetails> debtorDetails = debtorDetailsService.findById(id);
        debtorDetails.ifPresent(debtorDetails1 -> {
            updateTotalDebt(debtorDetails1.getId(), debtorDetails1.getDebt().multiply(new BigDecimal(-1)), userName);
            debtorHistoryService.saveEntityDebtorHistory(debtorDetails1);
        });
        log.debug("Delete DebtorDetails\nid : " + id);
        debtorDetailsService.deleteById(id);
    }
    public void addNewDebtor(String debtorName, BigDecimal debtValue, String reasonForTheDebt, String userName) {
        String actualUserName = userService.findUserName();
        if (isThisNameFree(debtorName, actualUserName)) {
            Debtor debtor = new Debtor();
            debtor.setName(debtorName);
            debtor.setTotalDebt(debtValue);
            debtor.setDateOfJoining(LocalDate.now());
            debtor.setUserName(userName);
            saveDebtor(debtor);

            debtorDetailsService.addNewDebtorDetails(debtorName, debtValue, reasonForTheDebt, userName, debtor);
        }
    }

    public Optional<Debtor> returnDebtorWithBiggestDebt(String userName) {
        Optional<Debtor> debtorFind = findByUserName(userName)
                .stream()
                .max(Comparator.comparing(Debtor::getTotalDebt));

        log.debug("Debtor with the biggest debt\nid : " + debtorFind.get().getId());

        //TODO looks weird, try to make it better
        debtorFind.ifPresentOrElse(debtor -> {
            log.debug("Debtor with the biggest debt\nid : " + debtor.getId() +
                    "\n Debt Value : " + debtor.getTotalDebt());
        }, () -> log.debug("Can't find debtor"));

        return debtorFind;
    }

    public Debtor findDebtorByName(String name) {
        return debtorRepo.findByName(name).get();
    }

}
