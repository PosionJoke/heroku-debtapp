package pl.bykowski.rectangleapp.services;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.model.dto.DebtorDetailsDTO;
import pl.bykowski.rectangleapp.repositories.DebtorRepo;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DebtorService {

    private static final Logger logger = Logger.getLogger(DebtorService.class);

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
        logger.debug("Save Debtor\nid : " + debtor.getId() + "\nname : " + debtor.getName());
        debtorRepo.save(debtor);
    }

    public String testReturnA(){
        return "A";
    }

    public Optional<Debtor> findById(Long id) {
        return debtorRepo.findById(id);
    }

    public List<Debtor> findByUserName(String name) {
        return debtorRepo.findByUserName(name);
    }

    private boolean isThisNameFree(String debtorName, String userName) {
        List<Debtor> debtorList = debtorRepo.findByUserName(userName);

        return debtorRepo.findByUserName(userName)
                .stream()
                .noneMatch(debtor -> debtor.getName().equalsIgnoreCase(debtorName));
    }

    //todo hard to test method
    @Transactional
    public void updateTotalDebtAndMakeNewDebtorDetails(DebtorDetailsDTO debtorDetails, Debtor debtor, String userName) {
        debtorDetailsService.addNewDebtorDetails(debtorDetails.getName(), debtorDetails.getDebt(), debtorDetails.getReasonForTheDebt(), userName, debtor);
        updateTotalDebt(debtor.getId(), debtorDetails.getDebt(), userName);
    }


    public BigDecimal updateTotalDebt(Long debtorId, BigDecimal debtValue, String userName) {
        Optional<Debtor> changedDebtor = debtorRepo.findById(debtorId);
        changedDebtor.ifPresent(debtor -> {
            BigDecimal newDebt = debtValue.add(debtor.getTotalDebt());
            debtor.setTotalDebt(newDebt);
            //todo check is this setUsername is necessary
            debtor.setUserName(userName);
            saveDebtor(debtor);
        });
        BigDecimal newDebtValue = changedDebtor.map(debtor -> debtor.getTotalDebt())
                .orElse(new BigDecimal(0));
        return newDebtValue;
    }

    //todo hard to test method
    public void deleteDebtorDetailsUpdateTotalDebtMakeNewDebtorHistory(Long id, Principal principal) {
        Optional<DebtorDetails> debtorDetails = debtorDetailsService.findById(id);
        debtorDetails.ifPresent(debtorDetails1 -> {
            updateTotalDebt(debtorDetails1.getId(), debtorDetails1.getDebt().multiply(new BigDecimal(-1)), principal.getName());
            debtorHistoryService.saveEntityDebtorHistory(debtorDetails1);
        });
        logger.debug("Delete DebtorDetails\nid : " + id);
        debtorDetailsService.deleteById(id);
    }

    public void addNewDebtor(String debtorName, BigDecimal debtValue, String reasonForTheDebt, String userName) {
        String actualUserName = userService.findUserName();
        if (isThisNameFree(debtorName, actualUserName)) {
            Debtor debtor = new Debtor();
            debtor.setName(debtorName);
            debtor.setTotalDebt(new BigDecimal(0));
            debtor.setDateOfJoining(LocalDate.now());
            debtor.setUserName(userName);
            saveDebtor(debtor);

            debtorDetailsService.addNewDebtorDetails(debtorName, debtValue, reasonForTheDebt, userName, debtor);
        }
    }

    public Optional<Debtor> returnDebtorWithBiggestDebt(Principal principal) {
        Optional<Debtor> debtorFind = debtorRepo.findByUserName(principal.getName())
                .stream()
                .max(Comparator.comparing(Debtor::getTotalDebt));
        logger.debug("Debtor with the biggest debt\nid : " + debtorFind.get().getId());

        //TODO looks weird, try to make it better
        debtorFind.ifPresentOrElse(debtor -> {
            logger.debug("Debtor with the biggest debt\nid : " + debtor.getId() +
                    "\n Debt Value : " + debtor.getTotalDebt());
        }, () -> logger.debug("Can't find debtor"));

        return debtorFind;
    }

    public Debtor findDebtorByName(String name) {
        return debtorRepo.findByName(name).get();
    }
}
