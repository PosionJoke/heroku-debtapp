package pl.bykowski.rectangleapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DebtorDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Builder.Default
    private BigDecimal debt = new BigDecimal(0);
    @Builder.Default
    private BigDecimal totalDebt = new BigDecimal(0);
    @Builder.Default
    private LocalDate date = LocalDate.now();
    private String reasonForTheDebt;
    private String userName;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime debtEndDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private Debtor debtor;

    //TODO CHECK IS IN EVERYWHERE USING FOR DATE LocalDate.now(), only 1 time i didnt use LocalDate.now()
//    public DebtorDetails(String name, BigDecimal debt, LocalDate date, String reasonForTheDebt, String userName, Debtor debtor) {
//        this.name = name;
//        this.debt = debt;
//        this.date = date;
//        this.reasonForTheDebt = reasonForTheDebt;
//        this.userName = userName;
//        this.debtor = debtor;
//    }

//    public DebtorDetails(String name, BigDecimal debt, LocalDate date, String reasonForTheDebt, String userName, LocalDateTime debtEndDate, Debtor debtor) {
//        this.name = name;
//        this.debt = debt;
//        this.date = date;
//        this.reasonForTheDebt = reasonForTheDebt;
//        this.userName = userName;
//        this.debtEndDate = debtEndDate;
//        this.debtor = debtor;
//    }
}
