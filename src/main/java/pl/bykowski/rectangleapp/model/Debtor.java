package pl.bykowski.rectangleapp.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class
Debtor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String registeredUser_name;
    private BigDecimal debt = new BigDecimal(0);
    private BigDecimal totalDebt = new BigDecimal(0);
    @Builder.Default
    private LocalDate dateOfJoining = LocalDate.now();
    private String userName;
    @OneToMany(mappedBy = "debtor", fetch = FetchType.LAZY)
    private Set<DebtorDetails> debtorDetailsList = new HashSet<>();
}
