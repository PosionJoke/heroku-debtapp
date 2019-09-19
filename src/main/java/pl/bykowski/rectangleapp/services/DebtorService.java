package pl.bykowski.rectangleapp.services;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.model.dto.DebtorDetailsDTO;
import pl.bykowski.rectangleapp.repositories.DebtorRepo;
import pl.bykowski.rectangleapp.model.Debtor;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Service
public class DebtorService {

    protected final Logger log = Logger.getLogger(getClass()); //org.apache.log4j.Logger;

    private DebtorRepo debtorRepo;
    private UserService userService;
    private DebtorDetailsService debtorDetailsService;
    private DebtorHistoryService debtorHistoryService;

    public DebtorService(DebtorRepo debtorRepo, UserService userService,
                         DebtorDetailsService debtorDetailsService, DebtorHistoryService debtorHistoryService) {
        this.debtorRepo = debtorRepo;
        this.userService = userService;
        this.debtorDetailsService = debtorDetailsService;
        this.debtorHistoryService = debtorHistoryService;
    }

    private void saveDebtor(Debtor debtor) {
        debtorRepo.save(debtor);
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

    // TODO fix findByIdŁ
    public float updateTotalDebt(Long debtorId, float debtValue, String userName) {
        Optional<Debtor> changedDebtor = debtorRepo.findById(debtorId);
        changedDebtor.ifPresent(debtor -> {
            float newDebt = debtValue + debtor.getTotalDebt();
            debtor.setTotalDebt(newDebt);
            debtor.setUserName(userName);
            saveDebtor(debtor);
            if (newDebt == 0) {
                log.error("new debt can't be 0");
            }
        });
        float newDebtValue = changedDebtor.map(debtor -> debtor.getTotalDebt())
                .orElse(0f);
        return newDebtValue;
    }

    //todo hard to test method
    public void deleteDebtorDetailsUpdateTotalDebtMakeNewDebtorHistory(Long id, Principal principal) {
        Optional<DebtorDetails> debtorDetails = debtorDetailsService.findById(id);
        debtorDetails.ifPresent(debtorDetails1 -> {
            updateTotalDebt(debtorDetails1.getId(), debtorDetails1.getDebt() * -1, principal.getName());
            debtorHistoryService.saveEntityDebtorHistory(debtorDetails1);
        });
        debtorDetailsService.deleteById(id);
    }

    public void addNewDebtor(String debtorName, float debtValue, String reasonForTheDebt, String userName) {
        String actualUserName = userService.findUserName();
        if (isThisNameFree(debtorName, actualUserName)) {
            Debtor debtor = new Debtor();
            debtor.setName(debtorName);
            debtor.setTotalDebt((debtValue + debtor.getTotalDebt()));
            debtor.setDate(LocalDate.now());
            debtor.setUserName(userName);
            debtorRepo.save(debtor);

            debtorDetailsService.addNewDebtorDetails(debtorName, debtValue, reasonForTheDebt, userName, debtor);
        }
    }

    //TODO LOGGERS PLS
    public Optional<Debtor> returnDebtorWithBiggestDebt(Principal principal) {
        return debtorRepo.findByUserName(principal.getName())
                .stream()
                .max(Comparator.comparing(Debtor::getTotalDebt));
    }

    public Debtor findDebtorByName(String name) {
        return debtorRepo.findByName(name);
    }
}
