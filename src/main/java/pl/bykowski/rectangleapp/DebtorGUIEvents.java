package pl.bykowski.rectangleapp;

import pl.bykowski.rectangleapp.Repositories.RepoStruct.DebtorHistory;
import pl.bykowski.rectangleapp.Repositories.RepoInterfaces.DebtorHistoryRepo;
import pl.bykowski.rectangleapp.Repositories.RepoStruct.Debtor;
import pl.bykowski.rectangleapp.Repositories.RepoInterfaces.DebtorRepo;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import pl.bykowski.rectangleapp.Repositories.RepoStruct.DebtorDetails;
import pl.bykowski.rectangleapp.Repositories.RepoInterfaces.DebtorDetailsRepo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

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

        long daysBetween = DAYS.between(debtorDetailsCopy.getDate(), LocalDate.now());

        debtorHistoryNew.setTimeOfDebt(daysBetween);

        debtorHistoryRepo.save(debtorHistoryNew);
        debtorDetailsRepo.delete(debtorDetailsRepo.findByNameAndId(textFieldName.getValue(), Long.parseLong(textFieldIdDebt.getValue())).get(0));
    }

    public static void main(String[] args) {
        LocalDate localDate = LocalDate.now();
        System.out.println("localDate -> " + localDate);
        localDate = localDate.minusDays(10);
        localDate = localDate.minusDays(5);
        localDate = localDate.minusMonths(0);
        localDate = localDate.minusYears(2018);
        System.out.println("localDate -> " + localDate);
        System.out.println(" ");

        LocalDate localDate1 = LocalDate.now();
        int yearFromlocalDate1 = localDate1.getYear();
        int dayFromlocalDate1 = localDate1.getDayOfMonth();
        int monthFromlocalDate1 = localDate1.getMonthValue();
        System.out.println("yearFromlocalDate1 - " + yearFromlocalDate1);
        System.out.println("dayFromlocalDate1 " + dayFromlocalDate1);
        System.out.println("monthFromlocalDate1 - " + monthFromlocalDate1);
        System.out.println("localDate1 -> " + localDate1);
        System.out.println("localDate -> " + localDate);

        System.out.println("@@@ @@@ @@@");

        localDate1 = localDate1.minusYears(localDate.getYear());
        System.out.println("@@ localDate.getYear() @@ " + localDate.getYear());
        localDate1 = localDate1.minusMonths(localDate.getMonthValue());
        System.out.println("@@ localDate.getMonthValue() @@ " + localDate.getMonthValue());
        localDate1 = localDate1.minusDays(localDate.getDayOfMonth());
        System.out.println("@@ localDate.getDayOfMonth(localDateNow) @@ " + localDate.getDayOfMonth());

        System.out.println("localDate1 after changes -> " + localDate1);

        System.out.println(" ~~ ~~ ~~ ");




        LocalDate localDateOld = LocalDate.of(2019,6,22);
        System.out.println("localDateOld -> "  +  localDateOld);

        LocalDate localDateNow = LocalDate.now();

        LocalDate localDateNowCopu = localDateNow;

        System.out.println("localDateNew -> "  +  localDateNow + "\n");
        int getDatofYear = localDateOld.getDayOfYear();
        System.out.println(" getDayOfYear " + getDatofYear);

        int dayOld = localDateOld.getDayOfMonth();
        int monthOld = localDateOld.getMonthValue();
        int yearOld = localDateOld.getYear();

//        localDateNow = localDateNow.minusDays(dayOld);
//        localDateNow = localDateNow.minusMonths(monthOld);
//        localDateNow = localDateNow.minusYears(yearOld);

        localDateNow = localDateNow.minusDays(getDatofYear);

        System.out.println("localDateNow before update -> -> " +localDateNowCopu + " \nlocalDateNow after update -> -> " + localDateNow);

        System.out.println(" ");
        System.out.println(" >>> ");

        LocalDate localDate10 = LocalDate.now();
        String stringA = localDate10.toString();
        LocalDate localDateOld10 = LocalDate.of(2019,6,22);
        String stringB = localDateOld10.toString();

        String startDate = "2016 01 02";
        String passedDate = "2016 02 29";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd");

        LocalDate date1 = LocalDate.parse(startDate, formatter);
        LocalDate date2 = LocalDate.parse(passedDate, formatter);

        long elapsedDays = DAYS.between(date1, date2);
        System.out.println(elapsedDays); // 58 (correct)


    }
}
