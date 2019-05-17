package pl.bykowski.rectangleapp;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

//wczytuje pliki z pliku konfiguracyjnego (resources > application.properties) do bazy danych
@Service
public class DebtorReader {

    private DebtorRepo debtorRepo;

    @Autowired
    public DebtorReader(DebtorRepo debtorRepo){

        this.debtorRepo = debtorRepo;

    }

    @Value("${name1}")
    private String name1;

    @Value("${debt1}")
    private Long debt1;

    @Value("${totalDebt1}")
    private Long totalDebt1;

//    @Value(("${date1}"))
//    private LocalDate date1;

    //METODA DODAJACA WYZEJ WYPISANE ELEMENTY
    // dzieki @EventListener z ApplicationReadyEvent.class, wykonuje ta metode podczas uruchomienia aplikacji
    @EventListener(ApplicationReadyEvent.class)
    public void addRectangles() {
//        Rectangle rectangle1 = new Rectangle(siteA1, siteB1);
//        Rectangle rectangle2 = new Rectangle(siteA2, siteB2);
//        Rectangle rectangle3 = new Rectangle(siteA3, siteB3);
//        Rectangle rectangle4 = new Rectangle(siteA4, siteB4);
        Debtor debtor1 = new Debtor(name1, debt1, totalDebt1);
        debtorRepo.save(debtor1);
        //System.out.println("~~~~~~~~~~~~~~~~~~~~~~``");
    }

}
