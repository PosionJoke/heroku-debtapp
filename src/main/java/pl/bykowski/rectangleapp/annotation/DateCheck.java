package pl.bykowski.rectangleapp.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = DateCheckValidator.class)
public @interface DateCheck {
    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
