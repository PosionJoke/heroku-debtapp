package pl.bykowski.rectangleapp.RectangleStuff;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.bykowski.rectangleapp.RectangleStuff.DebtorStuff.Debtor;
import pl.bykowski.rectangleapp.RectangleStuff.DebtorStuff.DebtorRepo;

import java.util.List;

//aby korzystac z bibliotegi Vaadin nalezy dodac adnotacje @Route
@Route
public class DebtorGUI extends VerticalLayout {

    //aby dodawac, korzystac z repo nalezy dodac tylko zmienna repo bez jej instancji
    private DebtorRepo debtorRepo;

    //definiowanie pol jakie maja byc w GUI
    private TextField textFieldName;
    private TextField textFieldDebt;

    private Button buttonInfo;
    private Button buttonAddDebtor;
    private Button buttonAddDebt;


    private TextArea areaInfo;

    @Autowired
    //inicjalizacja REPO jak i wszystkich innych pol
    public DebtorGUI (DebtorRepo debtorRepo){
        this.debtorRepo = debtorRepo;

        this.textFieldName = new TextField("Type Name: ");
        this.textFieldDebt = new TextField("Type Debt: ");

        this.buttonInfo = new Button("Show info by name");
        this.buttonAddDebtor = new Button("Add new Debtor");
        this.buttonAddDebt = new Button("Add new Debt");

        this.areaInfo = new TextArea("Info");

        //dodawanie eventu do przycisku
        buttonInfo.addClickListener(buttonClickEvent -> showInfo());
        buttonAddDebtor.addClickListener(buttonClickEvent -> addNewDebtor());
        buttonAddDebt.addClickListener(buttonClickEvent -> addNewDebt());

        add(textFieldName);
        add(textFieldDebt);

        add(buttonInfo);
        add(buttonAddDebtor);
        add(buttonAddDebt);

        add(areaInfo);
    }
    //metoda do przycisku
    private void addNewDebt() {
        //jezeli wpisany uzytkownik nie istnieje
        List<Debtor> debtorList = debtorRepo.returnAllDebtors();
        boolean isNameFree = true;

        for(Debtor debtor : debtorList){
            if(debtor.getName().equalsIgnoreCase(textFieldName.getValue())){
                isNameFree = false;
            }
        }//end for each

        if(isNameFree == true){
            addNewDebtor();
            areaInfo.setValue(textFieldName.getValue() + " is added! \n Debt value -> " + textFieldDebt.getValue());
        }
        else {

            areaInfo.setValue("D1");
            float fa = 12121212;
            areaInfo.setValue("D2");
            debtorRepo.setNewTotalDebtByNameTest();
            areaInfo.setValue("D3");
            areaInfo.setValue(String.valueOf(debtorRepo.getTotalDebt("Jan")));

        }
    }

    //metoda do przycisku
    private void addNewDebtor() {
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
    private void showInfo() {
        areaInfo.setValue(debtorRepo.isThisUserEgsist(textFieldName.getValue()).toString());
        //info.setValue("xD");
    }

}
