package pl.bykowski.rectangleapp.gui.debtor_gui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToFloatConverter;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.bykowski.rectangleapp.DebtorService;
import pl.bykowski.rectangleapp.form.DebtorsLIstGUIForm;
import pl.bykowski.rectangleapp.repositories.repo_interfaces.DebtorDetailsRepo;
import pl.bykowski.rectangleapp.repositories.repo_struct.DebtorDetails;

import java.util.Collection;

@StyleSheet("/css/style.css")
@Route(value = "debtorlistgui")
public class DebtorsListGUI extends VerticalLayout {

    private DebtorService debtorService;

    private Button deleteDebtByIdButton;
    private Button backToMainViewButton;
    private Button editDebtByIdAndValueButton;

    private TextField idToDeleteTextField;
    private TextField newValueField;
    private TextField debtorNameField;

    private static final StringToFloatConverter DEBT_TO_FLOAT_CONVERTER = new StringToFloatConverter("Invalid debt format");
    private static final StringToLongConverter DEBT_TO_LONG_CONVERTER = new StringToLongConverter("Invalid debt format");
    private Binder<DebtorsLIstGUIForm> debtorsLIstGUIFormBinder;

    Grid<DebtorDetails> grid = new Grid<>(DebtorDetails.class);

    @Autowired
    public DebtorsListGUI(DebtorDetailsRepo debtorDetailsRepo, DebtorService debtorService) {



        grid.setItems((Collection<DebtorDetails>) debtorDetailsRepo.findAll());

        this.debtorService = debtorService;

        this.idToDeleteTextField = new TextField("Delete by ID");
        this.newValueField = new TextField("Add value");
        this.debtorNameField = new TextField("Debtor Name");

        this.deleteDebtByIdButton = new Button("Delete debt by ID");
        this.backToMainViewButton = new Button("Back to main view");
        this.editDebtByIdAndValueButton = new Button("Edit debt by ID and new value");

        debtorsLIstGUIFormBinder = new Binder<>();
        debtorsLIstGUIFormBinder.forField(debtorNameField).bind(DebtorsLIstGUIForm::getDebtorNameField, DebtorsLIstGUIForm::setDebtorNameField);
        debtorsLIstGUIFormBinder.forField(newValueField).withConverter(DEBT_TO_FLOAT_CONVERTER).bind(DebtorsLIstGUIForm::getNewValueField, DebtorsLIstGUIForm::setNewValueField);
        debtorsLIstGUIFormBinder.forField(idToDeleteTextField).withConverter(DEBT_TO_LONG_CONVERTER).bind(DebtorsLIstGUIForm::getIdToDeleteTextField, DebtorsLIstGUIForm::setIdToDeleteTextField);
        debtorsLIstGUIFormBinder.setBean(new DebtorsLIstGUIForm());

        backToMainViewButton.addClickListener(e -> {
            backToMainViewButton.getUI().ifPresent(ui -> ui.navigate("debtorgui"));
        });

        deleteDebtByIdButton.addClickListener(buttonClickEvent -> {
            onDeleteDebtByIdButtonClick();
            grid.setItems((Collection<DebtorDetails>) debtorDetailsRepo.findAll());
        });

        editDebtByIdAndValueButton.addClickListener(buttonClickEvent -> {
            onEditDebtByIdAndValueButtonClick();
            grid.setItems((Collection<DebtorDetails>) debtorDetailsRepo.findAll());
        });


        add(deleteDebtByIdButton);
        add(grid);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.add(idToDeleteTextField);
        buttonsLayout.add(debtorNameField);
        buttonsLayout.add(newValueField);

        add(buttonsLayout);

        add(deleteDebtByIdButton);
        add(editDebtByIdAndValueButton);


        add(backToMainViewButton);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

    }

    private void onEditDebtByIdAndValueButtonClick() {
        String name = debtorsLIstGUIFormBinder.getBean().getDebtorNameField();
        float value = debtorsLIstGUIFormBinder.getBean().getNewValueField();
        Long id = debtorsLIstGUIFormBinder.getBean().getIdToDeleteTextField();

        debtorService.updateDebtByNewDebt(name, id, value);
//        grid.setItems((Collection<DebtorDetails>) debtorDetailsRepo.findAll());
    }

    private void onDeleteDebtByIdButtonClick() {
        Long id = debtorsLIstGUIFormBinder.getBean().getIdToDeleteTextField();
        debtorService.deleteDebtByID(id);
//        grid.setItems((Collection<DebtorDetails>) this.debtorDetailsRepo.findAll());
    }
}
