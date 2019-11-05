package pl.bykowski.rectangleapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebtorHistoryDTO implements CurrencyRate {
    @Size(min = 2, message = "Name should have at least 2 characters")
    private String name;
    private BigDecimal debt = new BigDecimal(0);
    private long timeOfDebt;
    private String reasonForTheDebt;
}
