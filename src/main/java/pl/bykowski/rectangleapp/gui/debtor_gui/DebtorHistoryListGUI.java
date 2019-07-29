package pl.bykowski.rectangleapp.gui.debtor_gui;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.bykowski.rectangleapp.Repositories.RepoStruct.DebtorHistory;
import pl.bykowski.rectangleapp.repositories.repo_interfaces.DebtorHistoryRepo;

import java.util.Collection;


@Route(value = "debtorhistrylistgui")
public class DebtorHistoryListGUI extends VerticalLayout {

    private Button backToMainViewButton;

    @Autowired
    public DebtorHistoryListGUI(DebtorHistoryRepo debtorHistoryRepo) {

        Grid<pl.bykowski.rectangleapp.Repositories.RepoStruct.DebtorHistory> grid = new Grid<>(pl.bykowski.rectangleapp.Repositories.RepoStruct.DebtorHistory.class);
        grid.setItems((Collection<DebtorHistory>) debtorHistoryRepo.findAll());

        this.backToMainViewButton = new Button("Back to main view");

        backToMainViewButton.addClickListener(e -> {
            backToMainViewButton.getUI().ifPresent(ui -> ui.navigate("debtorgui"));
        });


        add(grid);
        add(backToMainViewButton);
    }
}
