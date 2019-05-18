package pl.bykowski.rectangleapp.RectangleStuff.DebtorStuff;

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
    float debt;
    float totalDebt;
    LocalDate date;
    String dateNow;

    //nalezy zrobic bezargumentowy konstruktor
    public Debtor() {
    }

    public Debtor(String name, float debt, float totalDebt) {
        this.name = name;
        this.debt = debt;
        this.totalDebt = totalDebt;
        this.date = LocalDate.now();
        this.dateNow = String.valueOf(this.date);
//        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDateNow() {
        return dateNow;
    }

    public void setDateNow(String dateNow) {
        this.dateNow = dateNow;
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

//    public LocalDate getDate() {
//        return date;
//    }
//
//    public void setDate(LocalDate date) {
//        this.date = date;
//    }


    @Override
    public String toString() {
        return "Name -------> " + name + "\n" +
                "Total Debt -> " + totalDebt;
    }

    public static void main(String[] args) {
//        String s = String.valueOf(LocalDate.now());
//        System.out.println(s);
//
//        Debtor debtor = new Debtor("Jan", 300, 25);
//
//        System.out.println(debtor);

    }
}
