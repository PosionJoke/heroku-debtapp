package pl.bykowski.rectangleapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import pl.bykowski.rectangleapp.repositories.repo_struct.DebtorHistory;
import pl.bykowski.rectangleapp.repositories.repo_interfaces.DebtorHistoryRepo;
import pl.bykowski.rectangleapp.repositories.repo_struct.Debtor;
import pl.bykowski.rectangleapp.repositories.repo_interfaces.DebtorRepo;
import com.vaadin.flow.component.textfield.TextArea;
import pl.bykowski.rectangleapp.repositories.repo_struct.DebtorDetails;
import pl.bykowski.rectangleapp.repositories.repo_interfaces.DebtorDetailsRepo;
import pl.bykowski.rectangleapp.form.DebtorGUIForm;

import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Controller
public class DebtorGUIEvents {

    private DebtorGUIForm debtorGUIForm;

    private DebtorRepo debtorRepo;
    private DebtorDetailsRepo debtorDetailsRepo;
    private DebtorHistoryRepo debtorHistoryRepo;

    @Autowired
    public DebtorGUIEvents (DebtorRepo debtorRepo, DebtorDetailsRepo debtorDetailsRepo, DebtorHistoryRepo debtorHistoryRepo, DebtorGUIForm debtorGUIForm){
        this.debtorGUIForm = debtorGUIForm;

        this.debtorDetailsRepo = debtorDetailsRepo;
        this.debtorHistoryRepo = debtorHistoryRepo;
        this.debtorRepo = debtorRepo;
    }

    //metoda do przycisku
    public void addNewDebt(String textFieldName, float textFieldDebt, TextArea areaInfo, String reasonForTheDebt) {
        //jezeli wpisany uzytkownik nie istnieje, dodaj go i dopisz mu dług
        List<Debtor> debtorList = (List<Debtor>) debtorRepo.findAll();
        boolean isNameFree = true;

        for(Debtor debtor : debtorList){
            if(debtor.getName().equalsIgnoreCase(textFieldName)){
                isNameFree = false;
            }
        }//end for each

        //dodawanie uzytkownika
        if(isNameFree == true){
            addNewDebtor(textFieldName, textFieldDebt, areaInfo, reasonForTheDebt);
            areaInfo.setValue(textFieldName + " is added! \n Debt value -> " + textFieldDebt);
        }
        //w innym wypadku zaktualizuj jego dług o nową wartość
        else {
            //dodawanie do bazy Debtor
            for(Debtor debtor : debtorRepo.findByName(textFieldName)){
                float newDebt = textFieldDebt + debtor.getTotalDebt();
                debtor.setTotalDebt(newDebt);
                debtorRepo.save(debtor);
                areaInfo.setValue("New debt of " + debtor.getName() + " \nis equals to " + debtor.getTotalDebt());
            }
            //dodawanie do bazy DebtorDetails, skoro jestesmy tutja to znaczy ze dłużnik juz jest w bazie danych, nalezy zaaktualizowac DebtorDetails
            DebtorDetails debtorDetails = new DebtorDetails();
            debtorDetails.setName(textFieldName);
            debtorDetails.setDebt(textFieldDebt);
            debtorDetails.setDate(LocalDate.now());
            //debtorDetails.setTotalDebt(debtorDetails.getTotalDebt() + Integer.parseInt(textFieldDebt.getValue()));
            debtorDetails.setReasonForTheDebt(reasonForTheDebt);

            debtorDetailsRepo.save(debtorDetails);
            }
        }


    //metoda do przycisku
    public void addNewDebtor(String textFieldName, float textFieldDebt, TextArea areaInfo, String reasonForTheDebt) {

        List<Debtor> debtorList = (List<Debtor>) debtorRepo.findAll();
        boolean isNameFree = true;

        for(Debtor debtor : debtorList){
            if(debtor.getName().equalsIgnoreCase(textFieldName)){
                isNameFree = false;
            }
        }//end for each

        //jezeli imie nie jest uzywane, dodajemy nowego dluznika
        if(isNameFree == true){
            //nowy dluznik do DebtorRepo
            Debtor debtor = new Debtor();
            debtor.setName(textFieldName);
            debtor.setTotalDebt((textFieldDebt + debtor.getTotalDebt()));
            debtor.setDate(LocalDate.now());

            //nowy dluznik do DebtorDetailsRepo
            DebtorDetails debtorDetails = new DebtorDetails();
            debtorDetails.setName(textFieldName);
            //debtorDetails.setTotalDebt((Integer.parseInt(textFieldDebt.getValue()) + debtorDetails.getTotalDebt()));
            debtorDetails.setDate(LocalDate.now());
            debtorDetails.setDebt((textFieldDebt));
            debtorDetails.setReasonForTheDebt(reasonForTheDebt);

            debtorDetailsRepo.save(debtorDetails);
            debtorRepo.save(debtor);

            areaInfo.setValue(textFieldName + " is Added! :)");
        }else areaInfo.setValue("This Debtor arledy exist! :(");
    }

    //metoda do przycisku
    public void showInfo(String textFieldName, TextArea areaInfo) {
        //TODO: 07.06.19 Nalezy zmienic konkatenacje stringa, chyba to byl string buffor, teraz tworzymy nowy obiekt na kazda iteracje co jest nieefektywne
        String dataAndDebt = "================" + "\n" +
                             "   Debt list"     + "\n" +
                             "================";
        for(DebtorDetails debtorDetails : debtorDetailsRepo.findByName(textFieldName)){
            dataAndDebt += "\n" +
                    " ID of Debt ---->  " + debtorDetails.getId() + "\n" +
                    " Date ---> " + debtorDetails.getDate() + "\n" +
                    " Debt ---> " + debtorDetails.getDebt() + "\n" +
                    " Reason ---> " + debtorDetails.getReasonForTheDebt() +
                    "\n-----------------------------";
        }
        // TODO: 07.06.19 UWAGA zmien nazwe metody getDebtDate ^, jest ona pare linijek wyzej
        areaInfo.setValue("Name ---> " + debtorRepo.findByName(textFieldName).get(0).getName() + "\n" +
                          "Total debt ---> " + debtorRepo.findByName(textFieldName).get(0).getTotalDebt() + "\n" +
                dataAndDebt);
    }

    //metoda do przycisku
    public void updateDebtByNewDebt(String textFieldName, Long textFieldUpdate, float textFieldDebt) {
        for(DebtorDetails debtorDetails : debtorDetailsRepo.findByNameAndId(textFieldName, textFieldUpdate)){
            Float newDebt = debtorDetails.getDebt() + textFieldDebt;
            debtorDetails.setDebt(newDebt);
            if(newDebt <= 0){
                deleteDebtByID(textFieldName, textFieldUpdate);
            }
            else
            debtorDetailsRepo.save(debtorDetails);
        }
    }

    @Transactional
    public void deleteDebtByID(String textFieldName, Long textFieldIdDebt) {


        addDebtorFromDebtorDetailsToDebtorHistory(textFieldName, textFieldIdDebt);

        debtorDetailsRepo.delete(debtorDetailsRepo.findByNameAndId(textFieldName, textFieldIdDebt).get(0));
    }


    public void addDebtorFromDebtorDetailsToDebtorHistory(String textFieldName, Long textFieldIdDebt){
        DebtorDetails debtorDetailsCopy = debtorDetailsRepo.findByNameAndId(textFieldName, textFieldIdDebt).get(0);

        DebtorHistory debtorHistoryNew = new DebtorHistory();
        debtorHistoryNew.setDebt(debtorDetailsCopy.getDebt());
        debtorHistoryNew.setName(debtorDetailsCopy.getName());
        debtorHistoryNew.setReasonForTheDebt(debtorDetailsCopy.getReasonForTheDebt());

        long daysBetween = DAYS.between(debtorDetailsCopy.getDate(), LocalDate.now());

        debtorHistoryNew.setTimeOfDebt(daysBetween);

        debtorHistoryRepo.save(debtorHistoryNew);
    }
}
