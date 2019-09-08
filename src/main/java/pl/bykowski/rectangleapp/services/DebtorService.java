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
import java.util.Arrays;
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
        String currentPrincipalName = authentication.getName();

        return currentPrincipalName;
    }

    public String makeNewDebtorOrDebtorDetailsByNewDebt(String debtorName, float debtValue, String reasonForTheDebt, String userName) {
        String areaInfoValue = "";
        if (isThisNameFree(debtorName, findUserName())) {
            addNewDebtor(debtorName, debtValue, reasonForTheDebt, userName);
            areaInfoValue = (debtorName + " is added! \n Debt value -> " + debtValue);
        }
        else {
            areaInfoValue = updateTotalDebt(debtorName, debtValue, userName);
            addNewDebtorDetails(debtorName, debtValue, reasonForTheDebt, userName);
//            updateDebtorTotalDebt(debtorName, debtValue);
        }
        return areaInfoValue;
    }

    public void updateDebtorTotalDebt(String debtorName, float debtValue) {
        Debtor debtor = debtorRepo.findByName(debtorName);
        float newDebt = debtor.getTotalDebt() + debtValue;
        debtor.setTotalDebt(newDebt);
        debtorRepo.save(debtor);
    }

    public String updateTotalDebt(String debtorName, float debtValue, String userName) {
        List<Debtor> listDebtors = debtorRepo.findAll();
        String name = "Rober R";
        String nameM = "Magda P.";
        String nameG = "ggg g.";
        Debtor debtor = debtorRepo.findByName(name);
        Debtor debtor1 = debtorRepo.findByName("Magda P.");
        Debtor debtor2 = debtorRepo.findByName("ggg g.");
        Debtor changedDebtor = debtorRepo.findByName(debtorName);
        float newDebt = debtValue + changedDebtor.getTotalDebt();
        changedDebtor.setTotalDebt(newDebt);
        changedDebtor.setUserName(userName);
        debtorRepo.save(changedDebtor);

        String areaInfoValue = ("New debt of " + changedDebtor.getName() + " \nis equals to " + changedDebtor.getTotalDebt());
        return areaInfoValue;
    }

    public void addNewDebtorDetails(String debtorName, float debtValue, String reasonForTheDebt, String userName) {
        DebtorDetails debtorDetails = new DebtorDetails();
        debtorDetails.setName(debtorName);
        debtorDetails.setDebt(debtValue);
        debtorDetails.setDate(LocalDate.now());
        debtorDetails.setReasonForTheDebt(reasonForTheDebt);
        debtorDetails.setUserName(userName);

        debtorDetailsRepo.save(debtorDetails);
    }

    public String addNewDebtor(String debtorName, float debtValue, String reasonForTheDebt, String userName) {
        String areaInfoValue = "";

        if (isThisNameFree(debtorName, findUserName())) {
            Debtor debtor = new Debtor();
            debtor.setName(debtorName);
            debtor.setTotalDebt((debtValue + debtor.getTotalDebt()));
            debtor.setDate(LocalDate.now());
            debtor.setUserName(userName);

            addNewDebtorDetails(debtorName, debtValue, reasonForTheDebt, userName);
            debtorRepo.save(debtor);

            areaInfoValue = (debtorName + " is Added! :)");
        } else areaInfoValue = ("This Debtor arledy exist! :(");

        return areaInfoValue;
    }

    public String showInfo(String debtorName) {

        String areaInfoValue = "";

        String dataAndDebt = "================" + "\n" +
                "   Debt list" + "\n" +
                "================";

        DebtorDetails debtorDetails = debtorDetailsRepo.findByName(debtorName);

        dataAndDebt += "\n" +
                " ID of Debt ---->  " + debtorDetails.getId() + "\n" +
                " Date ---> " + debtorDetails.getDate() + "\n" +
                " Debt ---> " + debtorDetails.getDebt() + "\n" +
                " Reason ---> " + debtorDetails.getReasonForTheDebt() +
                "\n-----------------------------";

        areaInfoValue = ("Name ---> " + debtorRepo.findByName(debtorName).getName() + "\n" +
                "Total debt ---> " + debtorRepo.findByName(debtorName).getTotalDebt() + "\n" +
                dataAndDebt);
        return areaInfoValue;
    }

    @Transactional
    public void updateDebtByNewDebt(Long debtID, float debtValue) {
        Optional<DebtorDetails> debtorDetails = debtorDetailsRepo.findById(debtID);
        DebtorDetails debtorDetails1 = debtorDetails.get();

        setNewDebtDeleteIfDebtValueIsUnderZero(debtorDetails1, debtValue);
//        float newDebt = debtorDetails1.getDebt() + debtValue;

//        debtorDetails1.setDebt(newDebt);
//        debtorDetailsRepo.save(debtorDetails1);

        String debtorName = debtorDetails1.getName();
        updateTotalDebt(debtorName, debtValue, findUserName());
    }

    private void setNewTotalDebt(String debtorName, float debtValue){
        Debtor debtor = debtorRepo.findByName(debtorName);
        float newTotalDebt = debtor.getTotalDebt() + debtValue;
        debtor.setTotalDebt(newTotalDebt);
    }

    private void setNewDebtDeleteIfDebtValueIsUnderZero(DebtorDetails debtorDetails, float debtValue){
        checkDebtIsUnderZero(debtorDetails, debtValue);
    }

    private void checkDebtIsUnderZero(DebtorDetails debtorDetails, float debtValue){
        float newDebt = debtorDetails.getDebt() + debtValue;
        if(newDebt <= 0){
            debtorDetails.setDebt(0);
            deleteDebtByID(debtorDetails.getName(), debtorDetails.getId());
        }else {
            debtorDetails.setDebt(newDebt);
            debtorDetailsRepo.save(debtorDetails);
        }
    }

//TODO Please Adrian, learn to use LOGERS XD
    @Transactional
    public void deleteDebtor(Debtor debtor){
        if(debtor.getTotalDebt() == 0){
            debtorRepo.delete(debtor);
        }else System.out.println("Debtor " + debtor.getName() + " doesn't have total debt equals to 0");
    }

    @Transactional
    public void checkTotalDebtIsUnderZero(Debtor debtor, float debtValue){
        float newDebt = debtor.getTotalDebt() + debtValue;
        if(newDebt <= 0){
            debtor.setDebt(0);
            debtorRepo.save(debtor);
//            deleteDebtByID(debtor.getName(), debtor.getId());
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

    public void deleteDebtByID(Long debtorID, String userName) {
        Optional<DebtorDetails> debtorDetailsCopyOptional = debtorDetailsRepo.findById(debtorID);

        DebtorDetails debtorDetailsCopy = debtorDetailsCopyOptional.get();

        saveEntityDebtorHistory(debtorDetailsCopy);

        debtorDetailsRepo.deleteById(debtorID);
    }

    public void deleteFromDebtorHistoryById(Long debtorID) {
        debtorHistoryRepo.deleteById(debtorID);
    }
}
