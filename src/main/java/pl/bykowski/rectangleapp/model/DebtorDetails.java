package pl.bykowski.rectangleapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class DebtorDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal debt = new BigDecimal(0);
    private BigDecimal totalDebt = new BigDecimal(0);
    private LocalDate date;
    private String reasonForTheDebt;
    private String userName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate debtEndDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private Debtor debtor;

    public DebtorDetails(String name, BigDecimal debt, LocalDate date, String reasonForTheDebt, String userName, Debtor debtor) {
        this.name = name;
        this.debt = debt;
        this.date = date;
        this.reasonForTheDebt = reasonForTheDebt;
        this.userName = userName;
        this.debtor = debtor;
    }

    public DebtorDetails(String name, BigDecimal debt, LocalDate date, String reasonForTheDebt, String userName, LocalDate debtEndDate, Debtor debtor) {
        this.name = name;
        this.debt = debt;
        this.date = date;
        this.reasonForTheDebt = reasonForTheDebt;
        this.userName = userName;
        this.debtEndDate = debtEndDate;
        this.debtor = debtor;
    }
}
