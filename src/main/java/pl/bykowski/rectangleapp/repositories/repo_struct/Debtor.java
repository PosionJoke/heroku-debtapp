package pl.bykowski.rectangleapp.repositories.repo_struct;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
//Use annotation @Entity to mark that class as database
@Entity
public class
Debtor {
    //Annotation @Id makes spring shure that variable is the id of class
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private float debt;
    private float totalDebt;
    private LocalDate date;
    private String dateNow;

    //We need to make argument less constructor
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
