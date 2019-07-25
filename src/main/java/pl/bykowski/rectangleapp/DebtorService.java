package pl.bykowski.rectangleapp;

import com.vaadin.flow.component.textfield.TextArea;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import pl.bykowski.rectangleapp.form.DebtorGUIForm;
import pl.bykowski.rectangleapp.repositories.repo_interfaces.DebtorDetailsRepo;
import pl.bykowski.rectangleapp.repositories.repo_interfaces.DebtorHistoryRepo;
import pl.bykowski.rectangleapp.repositories.repo_interfaces.DebtorRepo;
import pl.bykowski.rectangleapp.repositories.repo_struct.Debtor;
import pl.bykowski.rectangleapp.repositories.repo_struct.DebtorDetails;
import pl.bykowski.rectangleapp.repositories.repo_struct.DebtorHistory;

import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Controller
public class DebtorService {

    //    private Binder<DebtorGUIForm> bean;
    private DebtorGUIForm debtorGUIForm;

    private DebtorRepo debtorRepo;
    private DebtorDetailsRepo debtorDetailsRepo;
    private DebtorHistoryRepo debtorHistoryRepo;

    public DebtorService(DebtorRepo debtorRepo, DebtorDetailsRepo debtorDetailsRepo, DebtorHistoryRepo debtorHistoryRepo) {
        this.debtorDetailsRepo = debtorDetailsRepo;
        this.debtorHistoryRepo = debtorHistoryRepo;
        this.debtorRepo = debtorRepo;
    }

    public void setDebtorGUIForm(DebtorGUIForm bean) {
        this.debtorGUIForm = bean;
    }

    public void addNewDebt(String debtorName, float debtValue, TextArea areaInfo, String reasonForTheDebt) {
        //jezeli wpisany uzytkownik nie istnieje, dodaj go i dopisz mu dług
        List<Debtor> debtorList = (List<Debtor>) debtorRepo.findAll();
        boolean isNameFree = true;

        for (Debtor debtor : debtorList) {
            if (debtor.getName().equalsIgnoreCase(debtorName)) {
                isNameFree = false;
            }
        }

        //dodawanie uzytkownika
        if (isNameFree) {
            addNewDebtor(debtorName, debtValue, areaInfo, reasonForTheDebt);
            areaInfo.setValue(debtorName + " is added! \n Debt value -> " + debtValue);
        }
        //w innym wypadku zaktualizuj jego dług o nową wartość
        else {
            //dodawanie do bazy Debtor
            for (Debtor debtor : debtorRepo.findByName(debtorName)) {
                float newDebt = debtValue + debtor.getTotalDebt();
                debtor.setTotalDebt(newDebt);
                debtorRepo.save(debtor);
                areaInfo.setValue("New debt of " + debtor.getName() + " \nis equals to " + debtor.getTotalDebt());
            }
            //dodawanie do bazy DebtorDetails, skoro jestesmy tutja to znaczy ze dłużnik juz jest w bazie danych, nalezy zaaktualizowac DebtorDetails
            DebtorDetails debtorDetails = new DebtorDetails();
            debtorDetails.setName(debtorName);
            debtorDetails.setDebt(debtValue);
            debtorDetails.setDate(LocalDate.now());
            //debtorDetails.setTotalDebt(debtorDetails.getTotalDebt() + Integer.parseInt(textFieldDebt.getValue()));
            debtorDetails.setReasonForTheDebt(reasonForTheDebt);

            debtorDetailsRepo.save(debtorDetails);
        }
    }

    public void addNewDebtor(String debtorName, float debtValue, TextArea areaInfo, String reasonForTheDebt) {

        List<Debtor> debtorList = (List<Debtor>) debtorRepo.findAll();
        boolean isNameFree = true;

        for (Debtor debtor : debtorList) {
            if (debtor.getName().equalsIgnoreCase(debtorName)) {
                isNameFree = false;
            }
        }

        //jezeli imie nie jest uzywane, dodajemy nowego dluznika
        if (isNameFree) {
            //nowy dluznik do DebtorRepo
            Debtor debtor = new Debtor();
            debtor.setName(debtorName);
            debtor.setTotalDebt((debtValue + debtor.getTotalDebt()));
            debtor.setDate(LocalDate.now());

            //nowy dluznik do DebtorDetailsRepo
            DebtorDetails debtorDetails = new DebtorDetails();
            debtorDetails.setName(debtorName);
            //debtorDetails.setTotalDebt((Integer.parseInt(textFieldDebt.getValue()) + debtorDetails.getTotalDebt()));
            debtorDetails.setDate(LocalDate.now());
            debtorDetails.setDebt((debtValue));
            debtorDetails.setReasonForTheDebt(reasonForTheDebt);

            debtorDetailsRepo.save(debtorDetails);
            debtorRepo.save(debtor);

            areaInfo.setValue(debtorName + " is Added! :)");
        } else areaInfo.setValue("This Debtor arledy exist! :(");
    }

    public void showInfo(String debtorName, TextArea areaInfo) {

        //TODO: 07.06.19 Nalezy zmienic konkatenacje stringa, chyba to byl string buffor, teraz tworzymy nowy obiekt na kazda iteracje co jest nieefektywne
        String dataAndDebt = "================" + "\n" +
                "   Debt list" + "\n" +
                "================";
        for (DebtorDetails debtorDetails : debtorDetailsRepo.findByName(debtorGUIForm.getTextFieldName())) {
            dataAndDebt += "\n" +
                    " ID of Debt ---->  " + debtorDetails.getId() + "\n" +
                    " Date ---> " + debtorDetails.getDate() + "\n" +
                    " Debt ---> " + debtorDetails.getDebt() + "\n" +
                    " Reason ---> " + debtorDetails.getReasonForTheDebt() +
                    "\n-----------------------------";
        }
        // TODO: 07.06.19 UWAGA zmien nazwe metody getDebtDate ^, jest ona pare linijek wyzej
        areaInfo.setValue("Name ---> " + debtorRepo.findByName(debtorGUIForm.getTextFieldName()).get(0).getName() + "\n" +
                "Total debt ---> " + debtorRepo.findByName(debtorGUIForm.getTextFieldName()).get(0).getTotalDebt() + "\n" +
                dataAndDebt);
    }

    public void updateDebtByNewDebt(String debtorName, Long debtID, float debtValue) {
        for (DebtorDetails debtorDetails : debtorDetailsRepo.findByNameAndId(debtorName, debtID)) {
            Float newDebt = debtorDetails.getDebt() + debtValue;
            debtorDetails.setDebt(newDebt);
            if (newDebt <= 0) {
                deleteDebtByID(debtorName, debtID);
            } else
                debtorDetailsRepo.save(debtorDetails);
        }
    }

    @Transactional
    public void deleteDebtByID(String debtorName, Long debtorID) {


        addDebtorFromDebtorDetailsToDebtorHistory(debtorName, debtorID);

        debtorDetailsRepo.delete(debtorDetailsRepo.findByNameAndId(debtorName, debtorID).get(0));
    }


    public void addDebtorFromDebtorDetailsToDebtorHistory(String debtorName, Long debtorID) {
        DebtorDetails debtorDetailsCopy = debtorDetailsRepo.findByNameAndId(debtorName, debtorID).get(0);

        DebtorHistory debtorHistoryNew = new DebtorHistory();
        debtorHistoryNew.setDebt(debtorDetailsCopy.getDebt());
        debtorHistoryNew.setName(debtorDetailsCopy.getName());
        debtorHistoryNew.setReasonForTheDebt(debtorDetailsCopy.getReasonForTheDebt());

        long daysBetween = DAYS.between(debtorDetailsCopy.getDate(), LocalDate.now());

        debtorHistoryNew.setTimeOfDebt(daysBetween);

        debtorHistoryRepo.save(debtorHistoryNew);
    }
}
