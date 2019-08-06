package pl.bykowski.rectangleapp.gui.debtor_gui;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.router.Route;
import pl.bykowski.rectangleapp.DebtorService;
import pl.bykowski.rectangleapp.repositories.repo_struct.DebtorHistory;
import pl.bykowski.rectangleapp.form.DebtorHistoryListGUIForm;
import pl.bykowski.rectangleapp.repositories.repo_interfaces.DebtorHistoryRepo;

import java.util.Collection;

@StyleSheet("/css/style.css")
@Route(value = "debtorhistrylistgui")
public class DebtorHistoryListGUI extends VerticalLayout {

    public static final String VIEW_NAME = "debtorhistrylistgui";

    private static final StringToLongConverter DEBT_TO_LONG_CONVERTER = new StringToLongConverter("Invalid debt format");
    private Binder<DebtorHistoryListGUIForm> debtorHistoryListGUIFormBinder;

    private transient DebtorService debtorService;

    private Button deleteDebtByIdButton;
    private Button backToMainViewButton;

    private TextField idToDeleteTextField;

    private Notification notification;


    public DebtorHistoryListGUI(DebtorHistoryRepo debtorHistoryRepo, DebtorService debtorService) {

        Grid<DebtorHistory> grid = new Grid<>(DebtorHistory.class);
        grid.setItems((Collection<DebtorHistory>) debtorHistoryRepo.findAll());

        this.debtorService = debtorService;

        this.idToDeleteTextField = new TextField("Delete by ID");

        this.deleteDebtByIdButton = new Button("Delete debt by ID");
        this.backToMainViewButton = new Button("Back to main view");

        this.notification = new Notification("", 3000);

        debtorHistoryListGUIFormBinder = new Binder<>();
        debtorHistoryListGUIFormBinder.forField(idToDeleteTextField)
                .withConverter(DEBT_TO_LONG_CONVERTER)
                .bind(DebtorHistoryListGUIForm::getIdToDeleteTextField, DebtorHistoryListGUIForm::setIdToDeleteTextField);
        debtorHistoryListGUIFormBinder.setBean(new DebtorHistoryListGUIForm());

        backToMainViewButton.addClickListener(e ->
                backToMainViewButton.getUI().ifPresent(ui -> ui.navigate(DebtorGUI.VIEW_NAME)));


        deleteDebtByIdButton.addClickListener(buttonClickEvent -> {
            onDeleteDebtByIdButtonClick();
            grid.setItems((Collection<DebtorHistory>) debtorHistoryRepo.findAll());
            notification.setText("Debt deleted");
            notification.open();
        });

        add(deleteDebtByIdButton);
        add(grid);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.add(idToDeleteTextField);

        add(buttonsLayout);

        add(deleteDebtByIdButton);

        add(backToMainViewButton);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    private void onDeleteDebtByIdButtonClick() {
        Long id = debtorHistoryListGUIFormBinder.getBean().getIdToDeleteTextField();
        debtorService.deleteFromDebtorHistoryById(id);
    }
}
