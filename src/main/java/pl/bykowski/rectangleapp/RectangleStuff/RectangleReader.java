package pl.bykowski.rectangleapp.RectangleStuff;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

//wczytuje pliki z pliku konfiguracyjnego (resources > application.properties) do bazy danych
@Service
public class RectangleReader {
/*
    private RectangleRepo rectangleRepo;

    //wstrzykuje instancje rectanglerepo
    @Autowired
    public RectangleReader(RectangleRepo rectangleRepo){
        this.rectangleRepo = rectangleRepo;
    }

    //adnotacja value pozwala wstrzynkac wartosc z pliku konfiguracyjnego do wskazanej wartosci
    @Value("${siteA1}")
    private int siteA1;
    @Value("${siteB1}")
    private int siteB1;

    @Value("${siteA2}")
    private int siteA2;
    @Value("${siteB2}")
    private int siteB2;

    @Value("${siteA3}")
    private int siteA3;
    @Value("${siteB3}")
    private int siteB3;

    @Value("${siteA4}")
    private int siteA4;
    @Value("${siteB4}")
    private int siteB4;

    //METODA DODAJACA WYZEJ WYPISANE ELEMENTY
    // dzieki @EventListener z ApplicationReadyEvent.class, wykonuje ta metode podczas uruchomienia aplikacji
    @EventListener(ApplicationReadyEvent.class)
    public void addRectangles() {
        Rectangle rectangle1 = new Rectangle(siteA1, siteB1);
        Rectangle rectangle2 = new Rectangle(siteA2, siteB2);
        Rectangle rectangle3 = new Rectangle(siteA3, siteB3);
        Rectangle rectangle4 = new Rectangle(siteA4, siteB4);
        rectangleRepo.save(rectangle1);
        rectangleRepo.save(rectangle2);
        rectangleRepo.save(rectangle3);
        rectangleRepo.save(rectangle4);
        //System.out.println("~~~~~~~~~~~~~~~~~~~~~~``");
    }
*/
}
