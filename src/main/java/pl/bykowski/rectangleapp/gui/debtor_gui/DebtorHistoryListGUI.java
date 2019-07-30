package pl.bykowski.rectangleapp.gui.debtor_gui;


import com.vaadin.flow.component.button.Button;
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
import pl.bykowski.rectangleapp.Repositories.RepoStruct.DebtorHistory;
import pl.bykowski.rectangleapp.form.DebtorHistoryListGUIForm;
import pl.bykowski.rectangleapp.repositories.repo_interfaces.DebtorHistoryRepo;

import java.util.Collection;


@Route(value = "debtorhistrylistgui")
public class DebtorHistoryListGUI extends VerticalLayout {

    private DebtorHistoryRepo debtorHistoryRepo;
    private DebtorService debtorService;

    private Button deleteDebtByIdButton;
    private Button backToMainViewButton;
//    private Button editDebtByIdAndValueButton;

    private TextField idToDeleteTextField;
    private TextField newValueField;
//    private TextField debtorNameField;

    private static final StringToFloatConverter DEBT_TO_FLOAT_CONVERTER = new StringToFloatConverter("Invalid debt format");
    private static final StringToLongConverter DEBT_TO_LONG_CONVERTER = new StringToLongConverter("Invalid debt format");
    private Binder<DebtorHistoryListGUIForm> debtorHistoryListGUIFormBinder;


    @Autowired
    public DebtorHistoryListGUI(DebtorHistoryRepo debtorHistoryRepo, DebtorService debtorService) {

        Grid<pl.bykowski.rectangleapp.Repositories.RepoStruct.DebtorHistory> grid = new Grid<>(pl.bykowski.rectangleapp.Repositories.RepoStruct.DebtorHistory.class);
        grid.setItems((Collection<DebtorHistory>) debtorHistoryRepo.findAll());

        this.debtorService = debtorService;
        this.debtorHistoryRepo = debtorHistoryRepo;

        this.idToDeleteTextField = new TextField("Delete by ID");
        this.newValueField = new TextField("Add value");
//        this.debtorNameField = new TextField("Debtor Name");

        this.deleteDebtByIdButton = new Button("Delete debt by ID");
        this.backToMainViewButton = new Button("Back to main view");
//        this.editDebtByIdAndValueButton = new Button("Edit debt by ID and new value");

        debtorHistoryListGUIFormBinder = new Binder<>();
//        debtorHistoryListGUIFormBinder.forField(debtorNameField).bind(DebtorHistoryListGUIForm::getDebtorNameField, DebtorHistoryListGUIForm::setDebtorNameField);
        debtorHistoryListGUIFormBinder.forField(newValueField).withConverter(DEBT_TO_FLOAT_CONVERTER).bind(DebtorHistoryListGUIForm::getNewValueField, DebtorHistoryListGUIForm::setNewValueField);
        debtorHistoryListGUIFormBinder.forField(idToDeleteTextField).withConverter(DEBT_TO_LONG_CONVERTER).bind(DebtorHistoryListGUIForm::getIdToDeleteTextField, DebtorHistoryListGUIForm::setIdToDeleteTextField);
        debtorHistoryListGUIFormBinder.setBean(new DebtorHistoryListGUIForm());

        backToMainViewButton.addClickListener(e -> {
            backToMainViewButton.getUI().ifPresent(ui -> ui.navigate("debtorgui"));
        });

        deleteDebtByIdButton.addClickListener(buttonClickEvent -> {
            onDeleteDebtByIdButtonClick();
            grid.setItems((Collection<DebtorHistory>) debtorHistoryRepo.findAll());
        });

//        editDebtByIdAndValueButton.addClickListener(buttonClickEvent -> {
//            onEditDebtByIdAndValueButtonClick();
//            grid.setItems((Collection<DebtorHistory>) debtorHistoryRepo.findAll());
//        });


        add(deleteDebtByIdButton);
        add(grid);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.add(idToDeleteTextField);
//        buttonsLayout.add(debtorNameField);
        buttonsLayout.add(newValueField);

        add(buttonsLayout);

        add(deleteDebtByIdButton);
//        add(editDebtByIdAndValueButton);


        add(backToMainViewButton);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

//    private void onEditDebtByIdAndValueButtonClick() {
//        String name = debtorNameField.getValue();
//        float value = Float.parseFloat(newValueField.getValue());
//        Long id = Long.parseLong(idToDeleteTextField.getValue());
//
//        debtorService.updateDebtByNewDebt(name, id, value);
//    }

    private void onDeleteDebtByIdButtonClick() {
        Long id = debtorHistoryListGUIFormBinder.getBean().getIdToDeleteTextField();
        debtorService.deleteFromDebtorHistoryById(id);
    }
}
