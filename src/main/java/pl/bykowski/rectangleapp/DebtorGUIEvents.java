package pl.bykowski.rectangleapp;

import pl.bykowski.rectangleapp.Repositories.DebtorRepository.Debtor;
import pl.bykowski.rectangleapp.Repositories.DebtorRepository.DebtorRepo;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import pl.bykowski.rectangleapp.Repositories.DeptorMoreInfoRepository.DebtorDetails;
import pl.bykowski.rectangleapp.Repositories.DeptorMoreInfoRepository.DebtorDetailsRepo;

import java.time.LocalDate;
import java.util.List;

public class DebtorGUIEvents {

//    @Autowired
//    DebtorDetailsRepo debtorRepo;

    //TextField testetst = new TextField("a");

    //metoda do przycisku
    public void addNewDebt(TextField textFieldName, TextField textFieldDebt, TextArea areaInfo, DebtorRepo debtorRepo, DebtorDetailsRepo debtorDetailsRepo, TextField reasonForTheDebt) {
        //jezeli wpisany uzytkownik nie istnieje, dodaj go i dopisz mu dług
        List<Debtor> debtorList = debtorRepo.returnAllDebtors();
        boolean isNameFree = true;

        for(Debtor debtor : debtorList){
            if(debtor.getName().equalsIgnoreCase(textFieldName.getValue())){
                isNameFree = false;
            }
        }//end for each

        //dodawanie uzytkownika
        if(isNameFree == true){
            addNewDebtor(textFieldName, textFieldDebt, areaInfo, debtorRepo, debtorDetailsRepo, reasonForTheDebt);
            areaInfo.setValue(textFieldName.getValue() + " is added! \n Debt value -> " + textFieldDebt.getValue());
        }
        //w innym wypadku zaktualizuj jego dług o nową wartość
        else {
            //dodawanie do bazy Debtor
            for(Debtor debtor : debtorRepo.getDebtorByName(textFieldName.getValue())){
                float newDebt = Integer.parseInt(textFieldDebt.getValue()) + debtor.getTotalDebt();
                debtor.setTotalDebt(newDebt);
                debtorRepo.save(debtor);
                areaInfo.setValue("New debt of " + debtor.getName() + " \nis equals to " + debtor.getTotalDebt());
            }
            //dodawanie do bazy DebtorDetails, skoro jestesmy tutja to znaczy ze dłużnik juz jest w bazie danych, nalezy zaaktualizowac DebtorDetails
            DebtorDetails debtorDetails = new DebtorDetails();
            debtorDetails.setName(textFieldName.getValue());
            debtorDetails.setDebt(Integer.parseInt(textFieldDebt.getValue()));
            debtorDetails.setDate(LocalDate.now());
            //debtorDetails.setTotalDebt(debtorDetails.getTotalDebt() + Integer.parseInt(textFieldDebt.getValue()));
            debtorDetails.setReasonForTheDebt(reasonForTheDebt.getValue());

            debtorDetailsRepo.save(debtorDetails);
            }
        }


    //metoda do przycisku
    public void addNewDebtor(TextField textFieldName, TextField textFieldDebt, TextArea areaInfo, DebtorRepo debtorRepo, DebtorDetailsRepo debtorDetailsRepo, TextField reasonForTheDebt) {

        List<Debtor> debtorList = debtorRepo.returnAllDebtors();
        boolean isNameFree = true;

        for(Debtor debtor : debtorList){
            if(debtor.getName().equalsIgnoreCase(textFieldName.getValue())){
                isNameFree = false;
            }
        }//end for each

        //jezeli imie nie jest uzywane, dodajemy nowego dluznika
        if(isNameFree == true){
            //nowy dluznik do DebtorRepo
            Debtor debtor = new Debtor();
            debtor.setName(textFieldName.getValue());
            debtor.setTotalDebt((Integer.parseInt(textFieldDebt.getValue()) + debtor.getTotalDebt()));
            debtor.setDate(LocalDate.now());

            //nowy dluznik do DebtorDetailsRepo
            DebtorDetails debtorDetails = new DebtorDetails();
            debtorDetails.setName(textFieldName.getValue());
            //debtorDetails.setTotalDebt((Integer.parseInt(textFieldDebt.getValue()) + debtorDetails.getTotalDebt()));
            debtorDetails.setDate(LocalDate.now());
            debtorDetails.setDebt((Integer.parseInt(textFieldDebt.getValue())));
            debtorDetails.setReasonForTheDebt(reasonForTheDebt.getValue());

            debtorDetailsRepo.save(debtorDetails);
            debtorRepo.save(debtor);

            areaInfo.setValue(textFieldName.getValue() + " is Added! :)");
        }else areaInfo.setValue("This Debtor arledy exist! :(");
    }

    //metoda do przycisku
    public void showInfo(TextField textFieldName, TextArea areaInfo, DebtorRepo debtorRepo) {
        areaInfo.setValue(debtorRepo.isThisUserEgsist(textFieldName.getValue()).toString());
        //info.setValue("xD");
    }

}