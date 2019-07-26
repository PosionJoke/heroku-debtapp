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

    private static final StringToFloatConverter DEBT_TO_FLOAT_COVERTER = new StringToFloatConverter("Invalid debt format");
    private static final StringToLongConverter DEBT_TO_LONG_COVERTER = new StringToLongConverter("Invalid debt format");

    private DebtorRepo debtorRepo;
    private DebtorDetailsRepo debtorDetailsRepo;
    private DebtorHistoryRepo debtorHistoryRepo;

    private Binder<DebtorGUIForm> debtorGUIFormBinder;
    private DebtorService debtorGUIEvents;

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

        debtorGUIFormBinder = new Binder<>();
        debtorGUIFormBinder.forField(textFieldName).bind(DebtorGUIForm::getTextFieldName, DebtorGUIForm::setTextFieldName);
        debtorGUIFormBinder.forField(textFieldReasonForTheDebt).bind(DebtorGUIForm::getTextFieldReasonForTheDebt, DebtorGUIForm::setTextFieldReasonForTheDebt);
        debtorGUIFormBinder.forField(textFieldDebt).withConverter(DEBT_TO_FLOAT_COVERTER).bind(DebtorGUIForm::getTextFieldDebt, DebtorGUIForm::setTextFieldDebt);
        debtorGUIFormBinder.forField(textFieldIdDebt).withConverter(DEBT_TO_LONG_COVERTER).bind(DebtorGUIForm::getTextFieldIdDebt, DebtorGUIForm::setTextFieldIdDebt);
        debtorGUIFormBinder.setBean(new DebtorGUIForm());


        buttonInfo.addClickListener(buttonClickEvent -> {
            String name = debtorGUIFormBinder.getBean().getTextFieldName();
            areaInfo.setValue(debtorGUIEvents.showInfo(name));

        });
        buttonAddDebtor.addClickListener(buttonClickEvent -> {
            String name = debtorGUIFormBinder.getBean().getTextFieldName();
            float debtValue = debtorGUIFormBinder.getBean().getTextFieldDebt();
            String reason = debtorGUIFormBinder.getBean().getTextFieldReasonForTheDebt();
            areaInfo.setValue(debtorGUIEvents.addNewDebtor(name, debtValue, reason));
        });
        buttonAddDebt.addClickListener(buttonClickEvent -> {
            String name = debtorGUIFormBinder.getBean().getTextFieldName();
            float debtValue = debtorGUIFormBinder.getBean().getTextFieldDebt();
            String reason = debtorGUIFormBinder.getBean().getTextFieldReasonForTheDebt();
            areaInfo.setValue(debtorGUIEvents.addNewDebt(name, debtValue, reason));
        });
        buttonUpdate.addClickListener(buttonClickEvent -> {
            String name = debtorGUIFormBinder.getBean().getTextFieldName();
            Long debtorID = debtorGUIFormBinder.getBean().getTextFieldIdDebt();
            float debtValue = debtorGUIFormBinder.getBean().getTextFieldDebt();
            debtorGUIEvents.updateDebtByNewDebt(name, debtorID, debtValue);
        });
        buttonDeleteDebt.addClickListener(buttonClickEvent -> {
            String name = debtorGUIFormBinder.getBean().getTextFieldName();
            Long debtorID = debtorGUIFormBinder.getBean().getTextFieldIdDebt();
            debtorGUIEvents.deleteDebtByID(name, debtorID);

            String info = debtorGUIEvents.showInfo(name);
            areaInfo.setValue(info);

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
