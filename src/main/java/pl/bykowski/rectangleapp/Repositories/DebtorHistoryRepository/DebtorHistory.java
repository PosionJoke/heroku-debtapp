package pl.bykowski.rectangleapp.Repositories.DebtorHistoryRepository;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@Entity
public class DebtorHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private float debt;
    private LocalDate timeOfDebt;
    private String reasonForTheDebt;

    //nalezy zrobic bezargumentowy konstruktor
    public DebtorHistory() {
    }

    public DebtorHistory(String name, float debt, LocalDate timeOfDebt, String reasonForTheDebt) {
        this.name = name;
        this.debt = debt;
        this.timeOfDebt = timeOfDebt;
        this.reasonForTheDebt = reasonForTheDebt;
    }
}
