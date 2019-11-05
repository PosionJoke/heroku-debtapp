package pl.bykowski.rectangleapp.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateCheckValidator implements ConstraintValidator<DateCheck, String> {
   public void initialize(DateCheck constraint) {
   }

   public boolean isValid(String dateString, ConstraintValidatorContext context) {
       if (!dateString.equals("")) {
           LocalDate debtEndDate = LocalDate.parse(dateString);
           LocalDateTime localDateTime111 = LocalDateTime.of(debtEndDate, LocalTime.now());

           return localDateTime111.isAfter(LocalDateTime.now());
       } else return false;
   }
}
