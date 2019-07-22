package pl.bykowski.rectangleapp.GUI.DebtorGUI;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.bykowski.rectangleapp.DebtorGUIEvents;
import pl.bykowski.rectangleapp.Repositories.RepoInterfaces.DebtorHistoryRepo;
import pl.bykowski.rectangleapp.Repositories.RepoInterfaces.DebtorRepo;
import pl.bykowski.rectangleapp.Repositories.RepoInterfaces.DebtorDetailsRepo;

//aby korzystac z bibliotegi Vaadin nalezy dodac adnotacje @Route
@Route
public class DebtorGUI extends VerticalLayout {

    //aby dodawac, korzystac z repo nalezy dodac tylko zmienna repo bez jej instancji
    private DebtorRepo debtorRepo;
    private DebtorDetailsRepo debtorDetailsRepo;
    private DebtorHistoryRepo debtorHistoryRepo;

    private DebtorGUIEvents debtorGUIEvents;

    //definiowanie pol jakie maja byc w GUI
    private TextField textFieldName;
    private TextField textFieldDebt;
    private TextField textFieldReasonForTheDebt;
    private TextField textFieldIdDebt;

    private Button buttonInfo;
    private Button buttonAddDebtor;
    private Button buttonAddDebt;
    private Button buttonUpdate;
    private Button buttonDeleteDebt;

    private TextArea areaInfo;

    @Autowired
    //inicjalizacja REPO jak i wszystkich innych pol
    public DebtorGUI (DebtorRepo debtorRepo, DebtorDetailsRepo debtorDetailsRepo, DebtorHistoryRepo debtorHistoryRepo, DebtorGUIEvents debtorGUIEvents){

        this.debtorRepo = debtorRepo;
        this.debtorDetailsRepo = debtorDetailsRepo;
        this.debtorHistoryRepo = debtorHistoryRepo;

        this.debtorGUIEvents = debtorGUIEvents;

        this.textFieldName = new TextField("Type Name: ");
        this.textFieldDebt = new TextField("Type Debt: ");
        this.textFieldReasonForTheDebt = new TextField("Type Reason for the debt: ");
        this.textFieldIdDebt = new TextField("Type ID of debt: ");

        this.buttonInfo = new Button("Show info by name");
        this.buttonAddDebtor = new Button("Add new Debtor");
        this.buttonAddDebt = new Button("Add new Debt");
        this.buttonUpdate = new Button("Update debt");
        this.buttonDeleteDebt = new Button("Delete Debt by ID ");

        this.areaInfo = new TextArea("Info");

        //dodawanie eventu do przycisku
        buttonInfo.addClickListener(buttonClickEvent -> {
            debtorGUIEvents.showInfo(textFieldName, areaInfo);
        });
        buttonAddDebtor.addClickListener(buttonClickEvent -> {
            debtorGUIEvents.addNewDebtor(textFieldName, textFieldDebt, areaInfo, textFieldReasonForTheDebt);
        });
        buttonAddDebt.addClickListener(buttonClickEvent -> {
            debtorGUIEvents.addNewDebt(textFieldName, textFieldDebt, areaInfo, textFieldReasonForTheDebt);
        });
        buttonUpdate.addClickListener(buttonClickEvent -> {
           debtorGUIEvents.updateDebtByNewDebt(textFieldName, textFieldIdDebt, textFieldDebt);
           debtorGUIEvents.showInfo(textFieldName, areaInfo);
        });
        buttonDeleteDebt.addClickListener(buttonClickEvent -> {
            debtorGUIEvents.deleteDebtByID(textFieldName, textFieldIdDebt);
            debtorGUIEvents.showInfo(textFieldName, areaInfo);
        });

        add(textFieldName);
        add(textFieldDebt);
        add(textFieldReasonForTheDebt);
        add(textFieldIdDebt);

        add(buttonUpdate);
        add(buttonInfo);
        add(buttonAddDebtor);
        add(buttonAddDebt);
        add(buttonDeleteDebt);

        add(areaInfo);
    }
}
