package pl.bykowski.rectangleapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.bykowski.rectangleapp.model.CurrencyRate;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DebtorDTO implements CurrencyRate {
    private Long id;
    private String name;
    private BigDecimal totalDebt = new BigDecimal(0);
    private Long countOfDebts;

    @Override
    public BigDecimal getDebt() {
        return this.totalDebt;
    }

    @Override
    public void setDebt(BigDecimal newDebt) {
        this.totalDebt = newDebt;
    }
}
