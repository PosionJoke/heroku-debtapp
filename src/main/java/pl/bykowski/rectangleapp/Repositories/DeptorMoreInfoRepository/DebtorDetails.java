package pl.bykowski.rectangleapp.Repositories.DeptorMoreInfoRepository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

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
        //this.totalDebt = totalDebt;
        this.date = date;
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

    public float getTotalDebt() {
        return totalDebt;
    }

    public void setTotalDebt(float totalDebt) {
        this.totalDebt = totalDebt;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getReasonForTheDebt() {
        return reasonForTheDebt;
    }

    public void setReasonForTheDebt(String reasonForTheDebt) {
        this.reasonForTheDebt = reasonForTheDebt;
    }
}
