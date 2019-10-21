package pl.bykowski.rectangleapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebtorDetailsDTO implements CurrencyRate{
    private Long id;
    private String name;
    private BigDecimal debt = new BigDecimal(0);
    private LocalDate date;
    private String reasonForTheDebt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate debtEndDate;

    @Override
    public void setDebt(BigDecimal newDebt) {
        this.debt = newDebt;
    }

    @Override
    public BigDecimal getDebt(){
        return this.debt;
    }
}
