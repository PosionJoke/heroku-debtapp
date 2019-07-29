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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Route(value = "debtorlistgui")
public class DebtorsListGUI extends VerticalLayout {

    DebtorService debtorService;

    Button deleteDebtByIdButton;
    TextField idToDeleteTextField;

    List<DebtorDetails> refreshGrid = new ArrayList<>();

    @Autowired
    public DebtorsListGUI(DebtorDetailsRepo debtorDetailsRepo, DebtorService debtorService) {

        Grid<DebtorDetails> grid = new Grid<>(DebtorDetails.class);
        grid.setItems((Collection<DebtorDetails>) debtorDetailsRepo.findAll());

        this.deleteDebtByIdButton = new Button("Delete debt by ID");
        this.debtorService = debtorService;
        this.idToDeleteTextField = new TextField("Delete by ID");

        deleteDebtByIdButton.addClickListener(buttonClickEvent -> {
            onDeleteDebtByIdButtonClick();

            grid.setItems((Collection<DebtorDetails>) debtorDetailsRepo.findAll());
        });
        add(deleteDebtByIdButton);
        add(grid);

        add(idToDeleteTextField);
        add(deleteDebtByIdButton);


    }

    private void onDeleteDebtByIdButtonClick() {

        Long id = Long.parseLong(idToDeleteTextField.getValue());
        debtorService.deleteDebtByID(id);

//        String info = debtorService.showInfo(name);
//        areaInfo.setValue(info);
    }
}
