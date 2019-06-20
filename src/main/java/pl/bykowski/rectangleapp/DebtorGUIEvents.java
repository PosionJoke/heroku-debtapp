package pl.bykowski.rectangleapp;

import pl.bykowski.rectangleapp.Repositories.DebtorHistoryRepository.DebtorHistory;
import pl.bykowski.rectangleapp.Repositories.DebtorHistoryRepository.DebtorHistoryRepo;
import pl.bykowski.rectangleapp.Repositories.DebtorRepository.Debtor;
import pl.bykowski.rectangleapp.Repositories.DebtorRepository.DebtorRepo;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import pl.bykowski.rectangleapp.Repositories.DeptorMoreInfoRepository.DebtorDetails;
import pl.bykowski.rectangleapp.Repositories.DeptorMoreInfoRepository.DebtorDetailsRepo;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class DebtorGUIEvents {

//    @Autowired
//    DebtorDetailsRepo debtorRepo;

    //TextField testetst = new TextField("a");

    //metoda do przycisku
    public void addNewDebt(TextField textFieldName, TextField textFieldDebt, TextArea areaInfo, DebtorRepo debtorRepo, DebtorDetailsRepo debtorDetailsRepo, TextField reasonForTheDebt) {
        //jezeli wpisany uzytkownik nie istnieje, dodaj go i dopisz mu dług
        List<Debtor> debtorList = (List<Debtor>) debtorRepo.findAll();
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
            for(Debtor debtor : debtorRepo.findByName(textFieldName.getValue())){
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

        List<Debtor> debtorList = (List<Debtor>) debtorRepo.findAll();
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
    public void showInfo(TextField textFieldName, TextArea areaInfo, DebtorRepo debtorRepo, DebtorDetailsRepo debtorDetailsRepo) {
        //TODO: 07.06.19 Nalezy zmienic konkatenacje stringa, chyba to byl string buffor, teraz tworzymy nowy obiekt na kazda iteracje co jest nieefektywne
        String dataAndDebt = "================" + "\n" +
                             "   Debt list"     + "\n" +
                             "================";
        for(DebtorDetails debtorDetails : debtorDetailsRepo.findByName(textFieldName.getValue())){
            dataAndDebt += "\n" +
                    " ID of Debt ---->  " + debtorDetails.getId() + "\n" +
                    " Date ---> " + debtorDetails.getDate() + "\n" +
                    " Debt ---> " + debtorDetails.getDebt() + "\n" +
                    " Reason ---> " + debtorDetails.getReasonForTheDebt() +
                    "\n-----------------------------";
        }
        // TODO: 07.06.19 UWAGA zmien nazwe metody getDebtDate ^, jest ona pare linijek wyzej
        areaInfo.setValue("Name ---> " + debtorRepo.findByName(textFieldName.getValue()).get(0).getName() + "\n" +
                          "Total debt ---> " + debtorRepo.findByName(textFieldName.getValue()).get(0).getTotalDebt() + "\n" +
                dataAndDebt);
    }

    //metoda do przycisku
    public void updateDebtByNewDebt(TextField textFieldName, TextField textFieldUpdate, TextField textFieldDebt, DebtorDetailsRepo debtorDetailsRepo) {
        for(DebtorDetails debtorDetails : debtorDetailsRepo.findByNameAndId(textFieldName.getValue(), Long.parseLong(textFieldUpdate.getValue()))){
            Float updatedDebt222 = debtorDetails.getDebt() + Float.parseFloat(textFieldDebt.getValue());
            debtorDetails.setDebt(updatedDebt222);
            debtorDetailsRepo.save(debtorDetails);
        }
    }

    //metoda do przycisku
    public void deleteDebtByID(TextField textFieldName, TextField textFieldIdDebt, DebtorDetailsRepo debtorDetailsRepo, DebtorHistoryRepo debtorHistoryRepo) {
        DebtorDetails debtorDetailsCopy = debtorDetailsRepo.findByNameAndId(textFieldName.getValue(), Long.parseLong(textFieldIdDebt.getValue())).get(0);

        DebtorHistory debtorHistoryNew = new DebtorHistory();
        debtorHistoryNew.setDebt(debtorDetailsCopy.getDebt());
        debtorHistoryNew.setName(debtorDetailsCopy.getName());
        debtorHistoryNew.setReasonForTheDebt(debtorDetailsCopy.getReasonForTheDebt());

        int dayDebt = debtorDetailsCopy.getDate().getDayOfMonth();
        int monthDebt = debtorDetailsCopy.getDate().getMonthValue();
        int yearDebt = debtorDetailsCopy.getDate().getYear();

        int dayNow = LocalDate.now().getDayOfMonth();
        int monthNow = LocalDate.now().getMonthValue();
        int yearNow = LocalDate.now().getYear();

        LocalDate newLocalDate = LocalDate.now();
        LocalDate localDate2 = newLocalDate.minusDays(dayDebt);
        localDate2 = localDate2.minusMonths(monthDebt);
//        localDate2 = localDate2.minusYears(yearDebt);
        //newLocalDate = newLocalDate.minusYears(yearDebt).minusMonths(monthDebt).minusDays(dayDebt);


        debtorHistoryNew.setTimeOfDebt(localDate2);

        debtorHistoryRepo.save(debtorHistoryNew);
        debtorDetailsRepo.delete(debtorDetailsRepo.findByNameAndId(textFieldName.getValue(), Long.parseLong(textFieldIdDebt.getValue())).get(0));
    }

    public static void main(String[] args) {
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate);
        localDate = localDate.minusDays(10);
        localDate = localDate.minusDays(5);
        localDate = localDate.minusMonths(6);
        localDate = localDate.minusYears(2018);
        System.out.println(localDate);

        LocalDate localDate1 = LocalDate.now();
        int xx = localDate1.getYear();
        int xx2 = localDate1.getDayOfMonth();
        int xx3 = localDate1.getMonthValue();
        System.out.println(xx);
        System.out.println(xx2);
        System.out.println(xx3);
        System.out.println("@@@ @@@ @@@");
        LocalDate localDate2 = LocalDate.now();
        System.out.println(localDate2);
        localDate2 = localDate2.minusDays(xx2);
        System.out.println(localDate2);
        localDate2 = localDate2.minusMonths(xx3);
        System.out.println(localDate2);
        localDate2 = localDate2.minusYears(xx);
        System.out.println(localDate2);
    }
}
