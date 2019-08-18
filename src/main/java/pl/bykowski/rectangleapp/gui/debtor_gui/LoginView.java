package pl.bykowski.rectangleapp.gui.debtor_gui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.router.Route;

@Route(value = LoginView.ROUTE)
public class LoginView extends VerticalLayout {
    public static final String ROUTE = "login";

    public LoginView(){
        UI.getCurrent().getPage().reload();
    }

}