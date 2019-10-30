package pl.bykowski.rectangleapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebtorDTO implements CurrencyRate {
    private Long id;
    private String name;
    private BigDecimal totalDebt = new BigDecimal(0);
    private Long countOfDebts;

    public DebtorDTO(Long id, String name, BigDecimal totalDebt) {
        this.id = id;
        this.name = name;
        this.totalDebt = totalDebt;
    }

    @Override
    public BigDecimal getDebt() {
        return this.totalDebt;
    }

    @Override
    public void setDebt(BigDecimal newDebt) {
        this.totalDebt = newDebt;
    }
}
