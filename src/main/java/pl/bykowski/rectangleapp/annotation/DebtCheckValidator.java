package pl.bykowski.rectangleapp.annotation;

import pl.bykowski.rectangleapp.model.dto.DebtorDetailsDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class DebtCheckValidator implements ConstraintValidator<DebtCheck, DebtorDetailsDTO> {
    public void initialize(DebtCheck constraint) {
    }

    @Override
    public boolean isValid(DebtorDetailsDTO debtorDetailsDTO, ConstraintValidatorContext context) {
        BigDecimal maxValue = new BigDecimal(1000000000);
        int result = debtorDetailsDTO.getDebt().compareTo(maxValue);

        return result < 1;
    }
}