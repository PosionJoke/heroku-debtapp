package pl.bykowski.rectangleapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class
Debtor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal debt = new BigDecimal(0);
    private BigDecimal totalDebt = new BigDecimal(0);
    private LocalDate dateOfJoining;
    private String userName;
    @OneToMany(mappedBy = "debtor", fetch = FetchType.LAZY)
    private Set<DebtorDetails> debtorDetailsList = new HashSet<>();
}
