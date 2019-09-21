package pl.bykowski.rectangleapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebtorDetailsDTO {
    private Long id;
    private String name;
    private BigDecimal debt = new BigDecimal(0);
    private LocalDate date;
    private String reasonForTheDebt;
}
