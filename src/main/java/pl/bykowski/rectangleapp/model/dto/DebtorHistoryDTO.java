package pl.bykowski.rectangleapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebtorHistoryDTO {
    private String name;
    private float debt;
    private long timeOfDebt;
    private String reasonForTheDebt;

}
