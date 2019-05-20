package pl.bykowski.rectangleapp.RectangleStuff;

import org.springframework.beans.factory.annotation.Autowired;
import pl.bykowski.rectangleapp.RectangleStuff.DebtorStuff.Debtor;
import pl.bykowski.rectangleapp.RectangleStuff.DebtorStuff.DebtorRepo;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import java.awt.*;
import java.util.List;

public class DebtorGUIEvents {

//    @Autowired
//    DebtorRepo debtorRepo;

    //TextField testetst = new TextField("a");

    //metoda do przycisku
    public void addNewDebt(TextField textFieldName, TextField textFieldDebt, TextArea areaInfo, DebtorRepo debtorRepo) {
        //jezeli wpisany uzytkownik nie istnieje, dodaj go i dopisz mu dług
        List<Debtor> debtorList = debtorRepo.returnAllDebtors();
        boolean isNameFree = true;

        for(Debtor debtor : debtorList){
            if(debtor.getName().equalsIgnoreCase(textFieldName.getValue())){
                isNameFree = false;
            }
        }//end for each

        if(isNameFree == true){
            addNewDebtor(textFieldName, textFieldDebt, areaInfo, debtorRepo);
            areaInfo.setValue(textFieldName.getValue() + " is added! \n Debt value -> " + textFieldDebt.getValue());
        }
        //w innym wypadku zaktualizuj jego dług o nową wartość
        else {
            for(Debtor debtor : debtorRepo.getDebtorByName(textFieldName.getValue())){
                float newDebt = Integer.parseInt(textFieldDebt.getValue()) + debtor.getTotalDebt();
                debtor.setTotalDebt(newDebt);
                debtorRepo.save(debtor);
                areaInfo.setValue("New debt of " + debtor.getName() + " \nis equals to " + debtor.getTotalDebt());
            }
        }
    }

    //metoda do przycisku
    public void addNewDebtor(TextField textFieldName, TextField textFieldDebt, TextArea areaInfo, DebtorRepo debtorRepo) {
        List<Debtor> debtorList = debtorRepo.returnAllDebtors();
        boolean isNameFree = true;

        for(Debtor debtor : debtorList){
            if(debtor.getName().equalsIgnoreCase(textFieldName.getValue())){
                isNameFree = false;
            }
        }//end for each

        if(isNameFree == true){
            Debtor debtor = new Debtor();
            debtor.setName(textFieldName.getValue());
            debtor.setTotalDebt((Integer.parseInt(textFieldDebt.getValue()) + debtor.getTotalDebt()));

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
