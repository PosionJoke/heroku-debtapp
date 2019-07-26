package pl.bykowski.rectangleapp.gui.debtor_gui;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.bykowski.rectangleapp.DebtorService;
import pl.bykowski.rectangleapp.domian.DebtorGUIFloatConverter;
import pl.bykowski.rectangleapp.domian.DebtorGUILongConverter;
import pl.bykowski.rectangleapp.form.DebtorGUIForm;
import pl.bykowski.rectangleapp.repositories.repo_interfaces.DebtorDetailsRepo;
import pl.bykowski.rectangleapp.repositories.repo_interfaces.DebtorHistoryRepo;
import pl.bykowski.rectangleapp.repositories.repo_interfaces.DebtorRepo;

//aby korzystac z biblioteki Vaadin nalezy dodac adnotacje @Route
@Route
public class DebtorGUI extends VerticalLayout {

    //aby dodawac, korzystac z repo nalezy dodac tylko zmienna repo bez jej instancji
    private DebtorRepo debtorRepo;
    private DebtorDetailsRepo debtorDetailsRepo;
    private DebtorHistoryRepo debtorHistoryRepo;

    private DebtorGUIFloatConverter debtorGUICFloatonverter;
    private DebtorGUILongConverter debtorGUILongConverter;
    private DebtorGUIForm debtorGUIForm;
    private Binder<DebtorGUIForm> debtorGUIFormBinder;
    private DebtorService debtorGUIEvents;

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
    public DebtorGUI(DebtorRepo debtorRepo, DebtorDetailsRepo debtorDetailsRepo, DebtorHistoryRepo debtorHistoryRepo, DebtorService debtorGUIEvents, DebtorGUIFloatConverter debtorGUIFloatConverter, DebtorGUILongConverter debtorGUILongConverter) {

        this.debtorRepo = debtorRepo;
        this.debtorDetailsRepo = debtorDetailsRepo;
        this.debtorHistoryRepo = debtorHistoryRepo;

        this.debtorGUIEvents = debtorGUIEvents;
//        this.debtorGUIForm = debtorGUIForm;
        this.debtorGUICFloatonverter = debtorGUIFloatConverter;
        this.debtorGUILongConverter = debtorGUILongConverter;

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
        debtorGUIFormBinder.forField(textFieldDebt).withConverter(this.debtorGUICFloatonverter).bind(DebtorGUIForm::getTextFieldDebt, DebtorGUIForm::setTextFieldDebt);
//        debtorGUIFormBinder.forField(textFieldIdDebt).withConverter(this.debtorGUILongConverter).bind(DebtorGUIForm::getTextFieldIdDebt, DebtorGUIForm::setTextFieldIdDebt);
        debtorGUIFormBinder.setBean(new DebtorGUIForm());


//        debtorGUIEvents.setDebtorGUIForm(debtorGUIFormBinder.getBean());

        //dodawanie eventu do przycisku
        buttonInfo.addClickListener(buttonClickEvent -> {
            setAreaInfo(areaInfo, debtorGUIEvents.showInfo(debtorGUIFormBinder.getBean().getTextFieldName()));
//            debtorGUIEvents.showInfo(debtorGUIFormBinder.getBean().getTextFieldName());
//            testMethodUsingBindValues();
        });
        buttonAddDebtor.addClickListener(buttonClickEvent -> {
            setAreaInfo(areaInfo, debtorGUIEvents.addNewDebtor(debtorGUIFormBinder.getBean().getTextFieldName(), debtorGUIFormBinder.getBean().getTextFieldDebt(), debtorGUIFormBinder.getBean().getTextFieldReasonForTheDebt()));
//            debtorGUIEvents.addNewDebtor(debtorGUIFormBinder.getBean().getTextFieldName(), debtorGUIFormBinder.getBean().getTextFieldDebt(), debtorGUIFormBinder.getBean().getTextFieldReasonForTheDebt());
        });
        buttonAddDebt.addClickListener(buttonClickEvent -> {
            setAreaInfo(areaInfo, debtorGUIEvents.addNewDebt(debtorGUIFormBinder.getBean().getTextFieldName(), debtorGUIFormBinder.getBean().getTextFieldDebt(), debtorGUIFormBinder.getBean().getTextFieldReasonForTheDebt()));
//            debtorGUIEvents.addNewDebt(debtorGUIFormBinder.getBean().getTextFieldName(), debtorGUIFormBinder.getBean().getTextFieldDebt(), debtorGUIFormBinder.getBean().getTextFieldReasonForTheDebt());
        });
        buttonUpdate.addClickListener(buttonClickEvent -> {
            debtorGUIEvents.updateDebtByNewDebt(debtorGUIFormBinder.getBean().getTextFieldName(), debtorGUIFormBinder.getBean().getTextFieldIdDebt(), debtorGUIFormBinder.getBean().getTextFieldDebt());
            setAreaInfo(areaInfo, debtorGUIEvents.showInfo(debtorGUIFormBinder.getBean().getTextFieldName()));
        });
        buttonDeleteDebt.addClickListener(buttonClickEvent -> {
            debtorGUIEvents.deleteDebtByID(debtorGUIFormBinder.getBean().getTextFieldName(), debtorGUIFormBinder.getBean().getTextFieldIdDebt());
            setAreaInfo(areaInfo, debtorGUIEvents.showInfo(debtorGUIFormBinder.getBean().getTextFieldName()));
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

    private void setAreaInfo(TextArea areaInfo, String value) {
        areaInfo.setValue(value);
    }
}
