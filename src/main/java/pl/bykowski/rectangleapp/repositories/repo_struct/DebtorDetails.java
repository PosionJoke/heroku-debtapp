package pl.bykowski.rectangleapp.repositories.repo_struct;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
//oznaczamy klase jako element bazy danych
@Entity
public class DebtorDetails {
    //jezeli to czytasz to chce wiedziec ze ta wiadomosc nic nie znaczy
    //oznaczamy pole jako ID dla bazy danych
    //autoincrement id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private float debt;
    private float totalDebt;
    private LocalDate date;
    private String reasonForTheDebt;

    //nalezy zrobic bezargumentowy konstruktor
    public DebtorDetails() {
    }

    public DebtorDetails(String name, float debt, LocalDate date, String reasonForTheDebt) {
        this.name = name;
        this.debt = debt;
        this.date = date;
        this.reasonForTheDebt = reasonForTheDebt;
    }
}
