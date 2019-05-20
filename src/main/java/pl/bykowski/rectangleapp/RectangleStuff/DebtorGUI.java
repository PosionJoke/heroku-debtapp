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
        buttonInfo.addClickListener(buttonClickEvent -> debtorGUIEvents.showInfo(textFieldName, areaInfo, debtorRepo));
        buttonAddDebtor.addClickListener(buttonClickEvent -> debtorGUIEvents.addNewDebtor(textFieldName, textFieldDebt, areaInfo, debtorRepo));
        buttonAddDebt.addClickListener(buttonClickEvent -> debtorGUIEvents.addNewDebt(textFieldName, textFieldDebt, areaInfo, debtorRepo));

        add(textFieldName);
        add(textFieldDebt);

        add(buttonInfo);
        add(buttonAddDebtor);
        add(buttonAddDebt);

        add(areaInfo);
    }

    DebtorGUIEvents debtorGUIEvents = new DebtorGUIEvents();

}
