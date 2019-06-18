package pl.bykowski.rectangleapp.Repositories.DebtorRepository;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
//oznaczamy klase jako element bazy danych
@Entity
public class
Debtor {

    //oznaczamy pole jako ID dla bazy danych
    //autoincrement id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private float debt;
    private float totalDebt;
    private LocalDate date;
    private String dateNow;

    //nalezy zrobic bezargumentowy konstruktor
    public Debtor() {
    }

    public Debtor(String name, float debt, float totalDebt) {
        this.name = name;
        this.debt = debt;
        this.totalDebt = totalDebt;
        this.date = LocalDate.now();
        this.dateNow = String.valueOf(this.date);
    }

    @Override
    public String toString() {
        return "Name -------> " + name + "\n" +
                "Total Debt -> " + totalDebt;
    }
}
