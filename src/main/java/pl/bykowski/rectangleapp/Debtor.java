package pl.bykowski.rectangleapp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;


//oznaczamy klase jako element bazy danych
@Entity
public class Debtor {

    //oznaczamy pole jako ID dla bazy danych
    //autoincrement id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String name;
    Long debt;
    Long totalDebt;
//    LocalDate date;

    //nalezy zrobic bezargumentowy konstruktor
    public Debtor() {
    }

    public Debtor(String name, Long debt, Long totalDebt) {
        this.name = name;
        this.debt = debt;
        this.totalDebt = totalDebt;
//        this.date = date;
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

    public Long getDebt() {
        return debt;
    }

    public void setDebt(Long debt) {
        this.debt = debt;
    }

    public Long getTotalDebt() {
        return totalDebt;
    }

    public void setTotalDebt(Long totalDebt) {
        this.totalDebt = totalDebt;
    }

//    public LocalDate getDate() {
//        return date;
//    }
//
//    public void setDate(LocalDate date) {
//        this.date = date;
//    }
}
