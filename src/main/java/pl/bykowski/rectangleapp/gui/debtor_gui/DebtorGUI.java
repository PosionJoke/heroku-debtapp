package pl.bykowski.rectangleapp.gui.debtor_gui;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToFloatConverter;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.bykowski.rectangleapp.DebtorService;
import pl.bykowski.rectangleapp.form.DebtorGUIForm;
import pl.bykowski.rectangleapp.repositories.repo_interfaces.DebtorDetailsRepo;
import pl.bykowski.rectangleapp.repositories.repo_interfaces.DebtorHistoryRepo;
import pl.bykowski.rectangleapp.repositories.repo_interfaces.DebtorRepo;

//To use vaadin library need use @Route add notation
@Route
public class DebtorGUI extends VerticalLayout {

    private static final StringToFloatConverter DEBT_TO_FLOAT_CONVERTER = new StringToFloatConverter("Invalid debt format");
    private static final StringToLongConverter DEBT_TO_LONG_CONVERTER = new StringToLongConverter("Invalid debt format");

    private DebtorRepo debtorRepo;
    private DebtorDetailsRepo debtorDetailsRepo;
    private DebtorHistoryRepo debtorHistoryRepo;
    private DebtorService debtorService;

    private Binder<DebtorGUIForm> debtorGUIFormBinder;

    //define variables which should be in GUI
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
    // initialization whole Repository and all of variables
    public DebtorGUI(DebtorRepo debtorRepo, DebtorDetailsRepo debtorDetailsRepo, DebtorHistoryRepo debtorHistoryRepo, DebtorService debtorGUIEvents) {

        this.debtorRepo = debtorRepo;
        this.debtorDetailsRepo = debtorDetailsRepo;
        this.debtorHistoryRepo = debtorHistoryRepo;

        this.debtorService = debtorGUIEvents;

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

        debtorGUIFormBinder = new Binder<>();
        debtorGUIFormBinder.forField(textFieldName).bind(DebtorGUIForm::getTextFieldName, DebtorGUIForm::setTextFieldName);
        debtorGUIFormBinder.forField(textFieldReasonForTheDebt).bind(DebtorGUIForm::getTextFieldReasonForTheDebt, DebtorGUIForm::setTextFieldReasonForTheDebt);
        debtorGUIFormBinder.forField(textFieldDebt).withConverter(DEBT_TO_FLOAT_CONVERTER).bind(DebtorGUIForm::getTextFieldDebt, DebtorGUIForm::setTextFieldDebt);
        debtorGUIFormBinder.forField(textFieldIdDebt).withConverter(DEBT_TO_LONG_CONVERTER).bind(DebtorGUIForm::getTextFieldIdDebt, DebtorGUIForm::setTextFieldIdDebt);
        debtorGUIFormBinder.setBean(new DebtorGUIForm());


        buttonInfo.addClickListener(buttonClickEvent -> onInfoButtonClick());

        buttonAddDebtor.addClickListener(buttonClickEvent -> onButtonAddDebtor());

        buttonAddDebt.addClickListener(buttonClickEvent -> onButtonAddDebt());

        buttonUpdate.addClickListener(buttonClickEvent -> onButtonUpdate());

        buttonDeleteDebt.addClickListener(buttonClickEvent -> onButtonDeleteDebt());

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

    private void onInfoButtonClick() {
        String name = debtorGUIFormBinder.getBean().getTextFieldName();
        areaInfo.setValue(debtorService.showInfo(name));
    }

    private void onButtonAddDebtor() {
        String name = debtorGUIFormBinder.getBean().getTextFieldName();
        float debtValue = debtorGUIFormBinder.getBean().getTextFieldDebt();
        String reason = debtorGUIFormBinder.getBean().getTextFieldReasonForTheDebt();
        areaInfo.setValue(debtorService.addNewDebtor(name, debtValue, reason));
    }

    private void onButtonAddDebt() {
        String name = debtorGUIFormBinder.getBean().getTextFieldName();
        float debtValue = debtorGUIFormBinder.getBean().getTextFieldDebt();
        String reason = debtorGUIFormBinder.getBean().getTextFieldReasonForTheDebt();
        areaInfo.setValue(debtorService.addNewDebt(name, debtValue, reason));
    }

    private void onButtonUpdate() {
        String name = debtorGUIFormBinder.getBean().getTextFieldName();
        Long debtorID = debtorGUIFormBinder.getBean().getTextFieldIdDebt();
        float debtValue = debtorGUIFormBinder.getBean().getTextFieldDebt();
        debtorService.updateDebtByNewDebt(name, debtorID, debtValue);
    }

    private void onButtonDeleteDebt() {
        String name = debtorGUIFormBinder.getBean().getTextFieldName();
        Long debtorID = debtorGUIFormBinder.getBean().getTextFieldIdDebt();
        debtorService.deleteDebtByID(name, debtorID);

        String info = debtorService.showInfo(name);
        areaInfo.setValue(info);
    }

}
