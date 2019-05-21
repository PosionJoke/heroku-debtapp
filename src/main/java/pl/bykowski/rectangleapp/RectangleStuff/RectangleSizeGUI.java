package pl.bykowski.rectangleapp.RectangleStuff;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route
public class RectangleSizeGUI extends VerticalLayout {

    private RectangleRepo rectangleRepo;
    private TextArea textFieldRectangles;
    private TextArea textField2Rectangles;

    //pole do wpisania i wyswietlania dla big rectangle
    private TextField textFieldSizeBig;
    private TextArea textAreaSizeResultBig;
    private Button buttonBig;

    //pole do wpisania i wyswietlania dla small rectangle
    private TextField textFieldSizeSmall;
    private TextArea textAreaSizeResultSmall;
    private Button buttonSmall;

    //powyższe pole i ten konsturktor pozwala nam DOSTAC sie do repozytorium i na nim dzialac!!!
    @Autowired
    public RectangleSizeGUI(RectangleRepo rectangleRepo) {
        this.rectangleRepo = rectangleRepo;
        textFieldRectangles = new TextArea("Duze Prostokaty");
        textField2Rectangles = new TextArea("Male Prostokaty");

        //pole do wpisania i wyswietlania dla big rectangle
        textFieldSizeBig = new TextField("Podaj wielkosc big: ");
        textAreaSizeResultBig = new TextArea("Wynik dla big");
        buttonBig = new Button("klik Big");
        //pole do wpisania i wyswietlania dla small rectangle
        textFieldSizeSmall = new TextField("Podaj wielkosc small: ");
        textAreaSizeResultSmall = new TextArea("Wynik dla small");
        buttonSmall = new Button("klik Small");


        textFieldRectangles.setValue(rectangleRepo.getBigRectangles().toString());
        textField2Rectangles.setValue(rectangleRepo.getSmallRectangles().toString());

        //pole do wpisania i wyswietlania dla big rectangle
        buttonBig.addClickListener(buttonClickEvent -> textAreaSizeResultBig.setValue(rectangleRepo.getBiggerSizeRectangles(Integer.parseInt(textFieldSizeBig.getValue())).toString()));
        //textAreaSizeResultBig.setValue(rectangleRepo.getBiggerSizeRectangles(Integer.parseInt(textFieldSizeBig.getValue())).toString());
        //pole do wpisania i wyswietlania dla small rectangle
        buttonSmall.addClickListener(buttonClickEvent -> textAreaSizeResultSmall.setValue(rectangleRepo.getSmallerSizeRectangles(Integer.parseInt(textFieldSizeSmall.getValue())).toString()));
        //textAreaSizeResultBig.setValue(rectangleRepo.getSmallerSizeRectangles(Integer.parseInt(textFieldSizeSmall.getValue())).toString());

        add(textFieldRectangles);
        add(textField2Rectangles);

        //pole do wpisania i wyswietlania dla big rectangle
        add(textFieldSizeBig);
        add(buttonBig);
        add(textAreaSizeResultBig);

        //pole do wpisania i wyswietlania dla small rectangle
        add(textFieldSizeSmall);
        add(buttonSmall);
        add(textAreaSizeResultSmall);
    }
}
