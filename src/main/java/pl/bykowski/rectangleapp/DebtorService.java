package pl.bykowski.rectangleapp;

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


    private boolean isThisNameFree(String debtorName) {
        List<Debtor> debtorList = (List<Debtor>) debtorRepo.findAll();

        for (Debtor debtor : debtorList) {
            if (debtor.getName().equalsIgnoreCase(debtorName)) {
                return false;
            }
        }
        return true;
    }

    public String addNewDebt(String debtorName, float debtValue, String reasonForTheDebt) {
        String areaInfoValue = "";
        //If we don't have any debts with this name, make him with new debt
        if (isThisNameFree(debtorName)) {
            addNewDebtor(debtorName, debtValue, reasonForTheDebt);
            areaInfoValue = (debtorName + " is added! \n Debt value -> " + debtValue);
        }
        //otherwise add to him extra debt
        else {
            //add new Debtor
            for (Debtor debtor : debtorRepo.findByName(debtorName)) {
                float newDebt = debtValue + debtor.getTotalDebt();
                debtor.setTotalDebt(newDebt);
                debtorRepo.save(debtor);
                areaInfoValue = ("New debt of " + debtor.getName() + " \nis equals to " + debtor.getTotalDebt());
            }
            //add a Debtor Details, if we are there its means Debtor Details should be update by new Debtor
            DebtorDetails debtorDetails = new DebtorDetails();
            debtorDetails.setName(debtorName);
            debtorDetails.setDebt(debtValue);
            debtorDetails.setDate(LocalDate.now());
            debtorDetails.setReasonForTheDebt(reasonForTheDebt);

            debtorDetailsRepo.save(debtorDetails);
        }
        return areaInfoValue;
    }

    public String addNewDebtor(String debtorName, float debtValue, String reasonForTheDebt) {
        String areaInfoValue = "";

        //If the name isn't use, add new debtor
        if (isThisNameFree(debtorName)) {
            Debtor debtor = new Debtor();
            debtor.setName(debtorName);
            debtor.setTotalDebt((debtValue + debtor.getTotalDebt()));
            debtor.setDate(LocalDate.now());

            DebtorDetails debtorDetails = new DebtorDetails();
            debtorDetails.setName(debtorName);
            debtorDetails.setDate(LocalDate.now());
            debtorDetails.setDebt((debtValue));
            debtorDetails.setReasonForTheDebt(reasonForTheDebt);

            debtorDetailsRepo.save(debtorDetails);
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
        for (DebtorDetails debtorDetails : debtorDetailsRepo.findByName(debtorName)) {
            dataAndDebt += "\n" +
                    " ID of Debt ---->  " + debtorDetails.getId() + "\n" +
                    " Date ---> " + debtorDetails.getDate() + "\n" +
                    " Debt ---> " + debtorDetails.getDebt() + "\n" +
                    " Reason ---> " + debtorDetails.getReasonForTheDebt() +
                    "\n-----------------------------";
        }
        areaInfoValue = ("Name ---> " + debtorRepo.findByName(debtorName).get(0).getName() + "\n" +
                "Total debt ---> " + debtorRepo.findByName(debtorName).get(0).getTotalDebt() + "\n" +
                dataAndDebt);
        return areaInfoValue;
    }

    @Transactional
    public void updateDebtByNewDebt(String debtorName, Long debtID, float debtValue) {
        for (DebtorDetails debtorDetails : debtorDetailsRepo.findByNameAndId(debtorName, debtID)) {
            float newDebt = debtorDetails.getDebt() + debtValue;
            debtorDetails.setDebt(newDebt);
            if (newDebt <= 0) {
                deleteDebtByID(debtorName, debtID);
            } else
                debtorDetailsRepo.save(debtorDetails);
        }
    }
    @Transactional
    public void deleteDebtByID(String debtorName, Long debtorID) {
        DebtorDetails debtorDetailsCopy = debtorDetailsRepo.findByNameAndId(debtorName, debtorID).get(0);
        DebtorHistory debtorHistoryNew = new DebtorHistory();
        debtorHistoryNew.setDebt(debtorDetailsCopy.getDebt());
        debtorHistoryNew.setName(debtorDetailsCopy.getName());
        debtorHistoryNew.setReasonForTheDebt(debtorDetailsCopy.getReasonForTheDebt());

        long daysBetween = DAYS.between(debtorDetailsCopy.getDate(), LocalDate.now());

        debtorHistoryNew.setTimeOfDebt(daysBetween);

        debtorHistoryRepo.save(debtorHistoryNew);

        debtorDetailsRepo.delete(debtorDetailsRepo.findByNameAndId(debtorName, debtorID).get(0));
    }
}
