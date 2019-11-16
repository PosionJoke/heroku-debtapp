package pl.bykowski.rectangleapp.annotation;

import org.springframework.stereotype.Component;
import pl.bykowski.rectangleapp.services.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

//@Component
public class IsThisUserNameExistValidator implements ConstraintValidator<IsThisUserNameExistCheck, String> {

    private final UserService userService;

    public IsThisUserNameExistValidator(UserService userService){
        this.userService = Objects.requireNonNull(userService, "userService must be not null");
    }

    @Override
    public void initialize(IsThisUserNameExistCheck constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return userService.isThisUserNameExist(value);
    }
}
