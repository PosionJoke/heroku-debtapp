package pl.bykowski.rectangleapp.annotation;

import pl.bykowski.rectangleapp.services.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class IsThisUserNameShouldExistValidator implements ConstraintValidator<IsThisUserNameShouldExistCheck, String> {

    private final UserService userService;
    protected boolean value;

    public IsThisUserNameShouldExistValidator(UserService userService) {
        this.userService = Objects.requireNonNull(userService, "userService must be not null");
    }

    @Override
    public void initialize(IsThisUserNameShouldExistCheck value) {
        this.value = value.value();
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        if(value){
            return userService.isThisUserNameExist(name);
        }else {
            return !userService.isThisUserNameExist(name);
        }
    }
}
