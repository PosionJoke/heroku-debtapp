package pl.bykowski.rectangleapp.gui.debtor_gui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.bykowski.rectangleapp.DebtorService;
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
        String name = debtorNameField.getValue();
        float value = Float.parseFloat(newValueField.getValue());
        Long id = Long.parseLong(idToDeleteTextField.getValue());

        debtorService.updateDebtByNewDebt(name, id, value);
//        grid.setItems((Collection<DebtorDetails>) debtorDetailsRepo.findAll());
    }

    private void onDeleteDebtByIdButtonClick() {
        Long id = Long.parseLong(idToDeleteTextField.getValue());
        debtorService.deleteDebtByID(id);
//        grid.setItems((Collection<DebtorDetails>) this.debtorDetailsRepo.findAll());
    }
}
