package pl.bykowski.rectangleapp.annotation;

import pl.bykowski.rectangleapp.model.dto.DebtorUserDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordCheckValidator implements ConstraintValidator<PasswordCheck, DebtorUserDTO> {
    public void initialize(PasswordCheck constraint) {
    }

    @Override
    public boolean isValid(DebtorUserDTO value, ConstraintValidatorContext context) {
        String password1 = value.getPassword1();
        String password2 = value.getPassword2();
        return password1.equals(password2);
    }
}