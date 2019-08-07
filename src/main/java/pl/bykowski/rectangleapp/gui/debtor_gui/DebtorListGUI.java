package pl.bykowski.rectangleapp.gui.debtor_gui;


import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import pl.bykowski.rectangleapp.repositories.repo_interfaces.DebtorRepo;
import pl.bykowski.rectangleapp.repositories.repo_struct.Debtor;

import java.util.Collection;

@StyleSheet("/css/style.css")
@Route(value = "debtorlistgui")
public class DebtorListGUI extends VerticalLayout {

    public static final String VIEW_NAME = "debtorlistgui";

    private DebtorRepo debtorRepo;

    public DebtorListGUI(DebtorRepo debtorRepo) {

        this.debtorRepo = debtorRepo;

        Grid<Debtor> grid = new Grid<>(Debtor.class);
        grid.setItems((Collection<Debtor>) debtorRepo.findAll());

        add(grid);
    }
}
