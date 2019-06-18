package pl.bykowski.rectangleapp.GUI.DebtorGUI;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.bykowski.rectangleapp.DebtorGUIEvents;
import pl.bykowski.rectangleapp.Repositories.DebtorRepository.DebtorRepo;
import pl.bykowski.rectangleapp.Repositories.DeptorMoreInfoRepository.DebtorDetailsRepo;

//aby korzystac z bibliotegi Vaadin nalezy dodac adnotacje @Route
@Route
public class DebtorGUI extends VerticalLayout {

    //aby dodawac, korzystac z repo nalezy dodac tylko zmienna repo bez jej instancji
    private DebtorRepo debtorRepo;
    private DebtorDetailsRepo debtorDetailsRepo;


    //definiowanie pol jakie maja byc w GUI
    private TextField textFieldName;
    private TextField textFieldDebt;
    private TextField textFieldReasonForTheDebt;

    private Button buttonInfo;
    private Button buttonAddDebtor;
    private Button buttonAddDebt;

    private TextArea areaInfo;

    @Autowired
    //inicjalizacja REPO jak i wszystkich innych pol
    public DebtorGUI (DebtorRepo debtorRepo, DebtorDetailsRepo debtorDetailsRepo){

        this.debtorRepo = debtorRepo;
        this.debtorDetailsRepo = debtorDetailsRepo;

        this.textFieldName = new TextField("Type Name: ");
        this.textFieldDebt = new TextField("Type Debt: ");
        this.textFieldReasonForTheDebt = new TextField("Type Reason for the debt: ");

        this.buttonInfo = new Button("Show info by name");
        this.buttonAddDebtor = new Button("Add new Debtor");
        this.buttonAddDebt = new Button("Add new Debt");

        this.areaInfo = new TextArea("Info");

        //dodawanie eventu do przycisku
        buttonInfo.addClickListener(buttonClickEvent -> {
            debtorGUIEvents.showInfo(textFieldName, areaInfo, debtorRepo, debtorDetailsRepo);
//            areaInfo.setValue("tests");
//            areaInfo.setValue(debtorRepo.findByName(textFieldName.getValue()).toString());
        });
        buttonAddDebtor.addClickListener(buttonClickEvent -> {
            debtorGUIEvents.addNewDebtor(textFieldName, textFieldDebt, areaInfo, debtorRepo, debtorDetailsRepo, textFieldReasonForTheDebt);
        });
        buttonAddDebt.addClickListener(buttonClickEvent -> {
            debtorGUIEvents.addNewDebt(textFieldName, textFieldDebt, areaInfo, debtorRepo, debtorDetailsRepo, textFieldReasonForTheDebt);
        });

        add(textFieldName);
        add(textFieldDebt);
        add(textFieldReasonForTheDebt);

        add(buttonInfo);
        add(buttonAddDebtor);
        add(buttonAddDebt);

        add(areaInfo);
    }

    DebtorGUIEvents debtorGUIEvents = new DebtorGUIEvents();

}
