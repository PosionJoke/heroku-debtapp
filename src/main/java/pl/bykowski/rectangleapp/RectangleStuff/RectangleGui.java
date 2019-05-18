package pl.bykowski.rectangleapp.RectangleStuff;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

//aby korzystac z bibliotegi Vaadin nalezy dodac adnotacje @Route
@Route
//dzieki VerticalLayout wszystko bedzie vertical
public class RectangleGui extends VerticalLayout {

    //aby moc dodawac WSTRZYKUJE interfejs
    private RectangleRepo rectangleRepo;

    //uzywane pola i DEKLAROWANIE ich w GUI w tym wypadku jest to textField i button
    private TextField textFieldHeight;
    private TextField textFieldWidth;
    private Button button;

    //aby dodac do konstruktora interfejs nalezy dodac adnotacje @Autowired i zainicjalizowac obiekt interfejsu
    @Autowired
    //inicjalizacja pól klasy, czyli obiektow do GUI
    public RectangleGui(RectangleRepo rectangleRepo) {
        this.rectangleRepo = rectangleRepo;
        this.textFieldHeight = new TextField("Podaj wyskokosc");
        this.textFieldWidth = new TextField("Podaj szerokosc");
        this.button = new Button("Dodaj!");

        button.addClickListener(buttonClickEvent -> addRectangle());

        add(textFieldHeight);
        add(textFieldWidth);
        add(button);
    }

    //metoda wywoływana przy przycisku button, dodajaca nowy Rectangle ktory w konstruktorze pobiera wartosci z pol
    public void addRectangle(){
        Rectangle rectangle = new Rectangle();
        rectangle.setHeight(Integer.parseInt(textFieldHeight.getValue()));
        rectangle.setWidth(Integer.parseInt(textFieldWidth.getValue()));

        //dzieki wstrzyknieci i utworzeniu instancji w konstruktorze mozemy wywolac na obiekcie Interfejsu metode save i w niej podac obiekt jaki chcemy zapisac
        rectangleRepo.save(rectangle);
    }

}
