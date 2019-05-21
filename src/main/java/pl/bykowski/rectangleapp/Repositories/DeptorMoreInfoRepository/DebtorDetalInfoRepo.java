package pl.bykowski.rectangleapp.Repositories.DeptorMoreInfoRepository;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;


@Entity
public class DebtorDetalInfoRepo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate debtDate;
    private float debt;
    private  String reasonForDebt;

    public DebtorDetalInfoRepo() {
    }

    public DebtorDetalInfoRepo(String name, LocalDate debtDate, float debt, String reasonForDebt) {
        this.name = name;
        this.debtDate = debtDate;
        this.debt = debt;
        this.reasonForDebt = reasonForDebt;
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

    public LocalDate getDebtDate() {
        return debtDate;
    }

    public void setDebtDate(LocalDate debtDate) {
        this.debtDate = debtDate;
    }

    public float getDebt() {
        return debt;
    }

    public void setDebt(float debt) {
        this.debt = debt;
    }

    public String getReasonForDebt() {
        return reasonForDebt;
    }

    public void setReasonForDebt(String reasonForDebt) {
        this.reasonForDebt = reasonForDebt;
    }
}
