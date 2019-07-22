package pl.bykowski.rectangleapp.Repositories.RepoStruct;

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
    private long timeOfDebt;
    private String reasonForTheDebt;


    //nalezy zrobic bezargumentowy konstruktor
    public DebtorHistory() {
    }

    public DebtorHistory(String name, float debt, long timeOfDebt, String reasonForTheDebt) {
        this.name = name;
        this.debt = debt;
        this.timeOfDebt = timeOfDebt;
        this.reasonForTheDebt = reasonForTheDebt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDebt() {
        return debt;
    }

    public void setDebt(float debt) {
        this.debt = debt;
    }

    public long getTimeOfDebt() {
        return timeOfDebt;
    }

    public void setTimeOfDebt(long timeOfDebt) {
        this.timeOfDebt = timeOfDebt;
    }

    public String getReasonForTheDebt() {
        return reasonForTheDebt;
    }

    public void setReasonForTheDebt(String reasonForTheDebt) {
        this.reasonForTheDebt = reasonForTheDebt;
    }
}
