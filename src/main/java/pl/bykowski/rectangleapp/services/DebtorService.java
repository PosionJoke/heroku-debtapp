package pl.bykowski.rectangleapp.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bykowski.rectangleapp.repositories.repo_interfaces.DebtorDetailsRepo;
import pl.bykowski.rectangleapp.repositories.repo_interfaces.DebtorHistoryRepo;
import pl.bykowski.rectangleapp.repositories.repo_interfaces.DebtorRepo;
import pl.bykowski.rectangleapp.repositories.repo_struct.Debtor;
import pl.bykowski.rectangleapp.repositories.repo_struct.DebtorDetails;
import pl.bykowski.rectangleapp.repositories.repo_struct.DebtorHistory;

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
        String currentPrincipalName = authentication.getName();

        return currentPrincipalName;
    }

    public String addNewDebt(String debtorName, float debtValue, String reasonForTheDebt, String userName) {
        String areaInfoValue = "";
        if (isThisNameFree(debtorName, findUserName())) {
            addNewDebtor(debtorName, debtValue, reasonForTheDebt, userName);
            areaInfoValue = (debtorName + " is added! \n Debt value -> " + debtValue);
        }
        else {
            areaInfoValue = addExtraDebt(debtorName, debtValue, userName);
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

    public String addExtraDebt(String debtorName, float debtValue, String userName) {
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

        float newDebt = debtorDetails1.getDebt() + debtValue;

        debtorDetails1.setDebt(newDebt);
        debtorDetailsRepo.save(debtorDetails1);

        String debtorName = debtorDetailsRepo.findById(debtID).get().getName();
        addExtraDebt(debtorName, debtValue, findUserName());
    }

    @Transactional
    public void deleteDebtByID(String debtorName, Long debtorID, String userName) {
        DebtorDetails debtorDetailsCopy = debtorDetailsRepo.findByNameAndId(debtorName, debtorID);
        DebtorHistory debtorHistoryNew = new DebtorHistory();
        debtorHistoryNew.setDebt(debtorDetailsCopy.getDebt());
        debtorHistoryNew.setName(debtorDetailsCopy.getName());
        debtorHistoryNew.setReasonForTheDebt(debtorDetailsCopy.getReasonForTheDebt());
        debtorDetailsCopy.setUserName(userName);

        long daysBetween = DAYS.between(debtorDetailsCopy.getDate(), LocalDate.now());

        debtorHistoryNew.setTimeOfDebt(daysBetween);

        debtorHistoryRepo.save(debtorHistoryNew);

        debtorDetailsRepo.delete(debtorDetailsRepo.findByNameAndId(debtorName, debtorID));
    }

    public void deleteDebtByID(Long debtorID, String userName) {
        Optional<DebtorDetails> debtorDetailsCopyOptional = debtorDetailsRepo.findById(debtorID);

        DebtorDetails debtorDetailsCopy = debtorDetailsCopyOptional.get();

        DebtorHistory debtorHistoryNew = new DebtorHistory();

        debtorHistoryNew.setDebt(debtorDetailsCopy.getDebt());
        debtorHistoryNew.setName(debtorDetailsCopy.getName());
        debtorHistoryNew.setReasonForTheDebt(debtorDetailsCopy.getReasonForTheDebt());
        debtorHistoryNew.setUserName(userName);

        long daysBetween = DAYS.between(debtorDetailsCopy.getDate(), LocalDate.now());

        debtorHistoryNew.setTimeOfDebt(daysBetween);

        debtorHistoryRepo.save(debtorHistoryNew);

        debtorDetailsRepo.deleteById(debtorID);
    }

    public void deleteFromDebtorHistoryById(Long debtorID) {
        debtorHistoryRepo.deleteById(debtorID);
    }
}
