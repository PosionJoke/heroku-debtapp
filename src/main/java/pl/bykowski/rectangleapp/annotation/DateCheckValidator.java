package pl.bykowski.rectangleapp.annotation;

import pl.bykowski.rectangleapp.model.dto.DebtorDetailsDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class DateCheckValidator implements ConstraintValidator<DateCheck, String> {
   public void initialize(DateCheck constraint) {
   }

   public boolean isValid(String dateString, ConstraintValidatorContext context) {
      LocalDate debtEndDate = LocalDate.parse(dateString);
      LocalDateTime localDateTime111 = LocalDateTime.of(debtEndDate, LocalTime.now());

      return localDateTime111.isAfter(LocalDateTime.now());
   }
}
