package pl.bykowski.rectangleapp.gui.debtor_gui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.bykowski.rectangleapp.DebtorService;
import pl.bykowski.rectangleapp.repositories.repo_interfaces.DebtorDetailsRepo;
import pl.bykowski.rectangleapp.repositories.repo_struct.DebtorDetails;

import java.util.Collection;

@Route(value = "debtorlistgui")
public class DebtorsListGUI extends VerticalLayout {

    DebtorService debtorService;

    Button deleteDebtByIdButton;
    Button backToMainViewButton;

    TextField idToDeleteTextField;

    @Autowired
    public DebtorsListGUI(DebtorDetailsRepo debtorDetailsRepo, DebtorService debtorService) {

        Grid<DebtorDetails> grid = new Grid<>(DebtorDetails.class);
        grid.setItems((Collection<DebtorDetails>) debtorDetailsRepo.findAll());

        this.debtorService = debtorService;
        this.idToDeleteTextField = new TextField("Delete by ID");
        this.deleteDebtByIdButton = new Button("Delete debt by ID");
        this.backToMainViewButton = new Button("Back to main view");


        deleteDebtByIdButton.addClickListener(buttonClickEvent -> {
            onDeleteDebtByIdButtonClick();

            grid.setItems((Collection<DebtorDetails>) debtorDetailsRepo.findAll());
        });

        backToMainViewButton.addClickListener(e -> {
            backToMainViewButton.getUI().ifPresent(ui -> ui.navigate("debtorgui"));
        });


        add(deleteDebtByIdButton);
        add(grid);

        add(idToDeleteTextField);
        add(deleteDebtByIdButton);
        add(backToMainViewButton);


    }

    private void onDeleteDebtByIdButtonClick() {
        Long id = Long.parseLong(idToDeleteTextField.getValue());
        debtorService.deleteDebtByID(id);
    }
}
