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
public class DebtorDetails {
    //Annotation @Id makes spring shure that variable is the id of class
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private float debt;
    private float totalDebt;
    private LocalDate date;
    private String reasonForTheDebt;

    //We need to make argument less constructor
    public DebtorDetails() {
    }

    public DebtorDetails(String name, float debt, LocalDate date, String reasonForTheDebt) {
        this.name = name;
        this.debt = debt;
        this.date = date;
        this.reasonForTheDebt = reasonForTheDebt;
    }
}
