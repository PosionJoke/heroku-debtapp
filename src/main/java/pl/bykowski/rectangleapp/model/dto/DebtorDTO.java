package pl.bykowski.rectangleapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebtorDTO {
    private Long id;
    private String name;
    private float totalDebt;
    private Long countOfDebts;

    public DebtorDTO(Long id, String name, float totalDebt) {
        this.id = id;
        this.name = name;
        this.totalDebt = totalDebt;
    }
}
