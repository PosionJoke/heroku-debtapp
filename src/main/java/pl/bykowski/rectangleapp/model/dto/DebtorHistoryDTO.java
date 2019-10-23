package pl.bykowski.rectangleapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebtorHistoryDTO implements CurrencyRate{
    private String name;
    private BigDecimal debt = new BigDecimal(0);
    private long timeOfDebt;
    private String reasonForTheDebt;
}
