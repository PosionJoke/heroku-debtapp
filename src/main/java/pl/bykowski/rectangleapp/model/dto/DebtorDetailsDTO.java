package pl.bykowski.rectangleapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import pl.bykowski.rectangleapp.annotation.DateCheck;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebtorDetailsDTO implements CurrencyRate {
    private Long id;
    private String name;
    @DecimalMax(value = "1000000000.0", message = "Max debt value cant be bigger then 1 000 000 000")
    @DecimalMin(value = "1.0", message = "Debt value cant lower then 1")
    private BigDecimal debt = new BigDecimal(0);
    private LocalDate date;
    @Size(min = 2, message = "Name should have at least 2 characters")
    private String reasonForTheDebt;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime debtEndDate;
    @DateCheck(message = "The date must contain future days")
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
