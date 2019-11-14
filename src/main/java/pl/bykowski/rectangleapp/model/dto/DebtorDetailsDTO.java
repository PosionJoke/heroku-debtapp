package pl.bykowski.rectangleapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import pl.bykowski.rectangleapp.annotation.DateCheck;
import pl.bykowski.rectangleapp.model.CurrencyRate;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DebtorDetailsDTO implements CurrencyRate {
    private Long id;
    @NotEmpty(message = "Name can't be empty")
    private String name;
    @DecimalMax(value = "1000000000.0", message = "Max debt value cant be bigger then 1 000 000 000")
    @DecimalMin(value = "1.0", message = "Debt value cant lower then 1")
    private BigDecimal debt = new BigDecimal(0);
    private LocalDate date;
    @NotEmpty(message = "Reason can't be empty")
    private String reasonForTheDebt;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime debtEndDate;
    @DateCheck(message = "You can sing only from next day")
    private String debtEndDateString;
    private String totalCountOfSeconds;

    @Override
    public void setDebt(BigDecimal newDebt) {
        this.debt = newDebt;
    }

    @Override
    public BigDecimal getDebt() {
        return this.debt;
    }
}
