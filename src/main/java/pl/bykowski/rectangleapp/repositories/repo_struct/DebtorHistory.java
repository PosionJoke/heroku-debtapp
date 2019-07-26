package pl.bykowski.rectangleapp.repositories.repo_struct;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class DebtorHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private float debt;
    private long timeOfDebt;
    private String reasonForTheDebt;

    public DebtorHistory() {
    }

    public DebtorHistory(String name, float debt, long timeOfDebt, String reasonForTheDebt) {
        this.name = name;
        this.debt = debt;
        this.timeOfDebt = timeOfDebt;
        this.reasonForTheDebt = reasonForTheDebt;
    }
}
