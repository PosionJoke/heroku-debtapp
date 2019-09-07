package pl.bykowski.rectangleapp.gui.debtor_gui;


import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import pl.bykowski.rectangleapp.repositories.DebtorRepo;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.services.DebtorService;

@StyleSheet("/css/style.css")
@Route(value = DebtorListGUI.VIEW_NAME)
public class DebtorListGUI extends VerticalLayout {

    public static final String VIEW_NAME = "debtorlistgui";

    private transient DebtorRepo debtorRepo;
    private transient DebtorService debtorService;

    public DebtorListGUI(DebtorRepo debtorRepo, DebtorService debtorService) {

        this.debtorRepo = debtorRepo;
        this.debtorService = debtorService;

        Grid<Debtor> grid = new Grid<>(Debtor.class);
        grid.setItems(debtorRepo.findByUserName(debtorService.findUserName()));
        add(grid);
    }
}
