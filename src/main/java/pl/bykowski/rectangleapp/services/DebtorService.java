package pl.bykowski.rectangleapp.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bykowski.rectangleapp.repositories.DebtorDetailsRepo;
import pl.bykowski.rectangleapp.repositories.DebtorHistoryRepo;
import pl.bykowski.rectangleapp.repositories.DebtorRepo;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.model.DebtorHistory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class DebtorService {

    private DebtorRepo debtorRepo;
    private DebtorDetailsRepo debtorDetailsRepo;
    private DebtorHistoryRepo debtorHistoryRepo;

    public DebtorService(DebtorRepo debtorRepo, DebtorDetailsRepo debtorDetailsRepo, DebtorHistoryRepo debtorHistoryRepo) {
        this.debtorDetailsRepo = debtorDetailsRepo;
        this.debtorHistoryRepo = debtorHistoryRepo;
        this.debtorRepo = debtorRepo;
    }

    private boolean isThisNameFree(String debtorName, String userName) {
        List<Debtor> debtorList = debtorRepo.findByUserName(userName);

        for (Debtor debtor : debtorList) {
            if (debtor.getName().equalsIgnoreCase(debtorName)) {
                return false;
            }
        }
        return true;
    }

    public String findUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private void updateTotalDebt(String debtorName, float debtValue, String userName) {
        Debtor changedDebtor = debtorRepo.findByName(debtorName);
        float newDebt = debtValue + changedDebtor.getTotalDebt();
        changedDebtor.setTotalDebt(newDebt);
        changedDebtor.setUserName(userName);
        debtorRepo.save(changedDebtor);
    }

    private void addNewDebtorDetails(String debtorName, float debtValue, String reasonForTheDebt, String userName) {
        DebtorDetails debtorDetails = new DebtorDetails();
        debtorDetails.setName(debtorName);
        debtorDetails.setDebt(debtValue);
        debtorDetails.setDate(LocalDate.now());
        debtorDetails.setReasonForTheDebt(reasonForTheDebt);
        debtorDetails.setUserName(userName);

        debtorDetailsRepo.save(debtorDetails);
    }

    public void addNewDebtor(String debtorName, float debtValue, String reasonForTheDebt, String userName) {
        if (isThisNameFree(debtorName, findUserName())) {
            Debtor debtor = new Debtor();
            debtor.setName(debtorName);
            debtor.setTotalDebt((debtValue + debtor.getTotalDebt()));
            debtor.setDate(LocalDate.now());
            debtor.setUserName(userName);

            addNewDebtorDetails(debtorName, debtValue, reasonForTheDebt, userName);
            debtorRepo.save(debtor);
        }
        //TODO HERE SHOULD BE A LOGGER -> ELSE LOGGER
    }

    @Transactional
    public void updateDebtorDetailsDebt(Long debtID, float debtValue) {
        Optional<DebtorDetails> debtorDetails = debtorDetailsRepo.findById(debtID);
        if(debtorDetails.isPresent()){
            DebtorDetails debtorDetails1 = debtorDetails.get();
            isThisDebtUnderZero(debtorDetails1, debtValue);
            String debtorName = debtorDetails1.getName();
            updateTotalDebt(debtorName, debtValue, findUserName());
        }
    }

    private void isThisDebtUnderZero(DebtorDetails debtorDetails, float debtValue){
        float newDebt = debtorDetails.getDebt() + debtValue;
        if(newDebt <= 0){
            debtorDetails.setDebt(0);
            deleteDebtByID(debtorDetails.getName(), debtorDetails.getId());
        }else {
            debtorDetails.setDebt(newDebt);
            debtorDetailsRepo.save(debtorDetails);
        }
    }

//TODO Please Adrian, learn to use LOGGERS XD
    @Transactional
    public void updateTotalDebt(Debtor debtor, float debtValue){
        float newDebt = debtor.getTotalDebt() + debtValue;
        if(newDebt <= 0){
            debtor.setDebt(0);
            debtorRepo.save(debtor);
        }else {
            debtor.setDebt(newDebt);
            debtorRepo.save(debtor);
        }
    }

    @Transactional
    public void deleteDebtByID(String debtorName, Long debtorID) {
        DebtorDetails debtorDetailsCopy = debtorDetailsRepo.findByNameAndId(debtorName, debtorID);

        saveEntityDebtorHistory(debtorDetailsCopy);

        debtorDetailsRepo.delete(debtorDetailsRepo.findByNameAndId(debtorName, debtorID));
    }

    private void saveEntityDebtorHistory(DebtorDetails debtorDetails) {
        DebtorHistory debtorHistory = new DebtorHistory();
        debtorHistory.setDebt(debtorDetails.getDebt());
        debtorHistory.setName(debtorDetails.getName());
        debtorHistory.setReasonForTheDebt(debtorDetails.getReasonForTheDebt());
        debtorHistory.setUserName(debtorDetails.getUserName());

        long daysBetween = DAYS.between(debtorDetails.getDate(), LocalDate.now());
        debtorHistory.setTimeOfDebt(daysBetween);

        debtorHistoryRepo.save(debtorHistory);
    }
}
