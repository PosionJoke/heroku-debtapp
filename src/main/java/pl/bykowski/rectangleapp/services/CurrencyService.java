package pl.bykowski.rectangleapp.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.dto.CurrencyRate;
import pl.bykowski.rectangleapp.model.dto.DebtorDTO;
import pl.bykowski.rectangleapp.model.dto.DebtorDetailsDTO;
import pl.bykowski.rectangleapp.model.dto.DebtorHistoryDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CurrencyService {

    private RestTemplate restTemplate;

    public CurrencyService(){
        this.restTemplate = new RestTemplate();
    }

    public String calculateCurrencyRates(String currencyOne, String currencyTwo){

        if(currencyOne == null){
            currencyOne = "PLN";
        }

        JsonNode jsonNode;

        //TODO this is wrong way, you should use ur own implementation of something like resthandler? (google it) from spring
        try{
            jsonNode = restTemplate.getForObject("https://api.exchangerate-api.com/v4/latest/" +
                    currencyOne.toUpperCase(), JsonNode.class).get("rates")
                    .get(currencyTwo.toUpperCase());
            return  jsonNode.asText();
        }catch (Exception ex){
            return "1";
        }
    }


    public <T extends CurrencyRate> List<T> setCurrencyRates(List<T> list, String currencyRate){
        list.stream()
                .forEach(listStream -> listStream.setDebt(
                        listStream.getDebt().divide(new BigDecimal(currencyRate), 2, RoundingMode.CEILING)
                ));
        return list;
    }

    public static void main(String[] args) {
        Date date = new Date();
        long time = date.getTime();
        System.out.println(new Date());
        System.out.println(time);

        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);

        LocalTime localTime = LocalTime.now();
        System.out.println(localTime);

        System.out.println("---------------------------");

        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = localDateTime.format(myFormatObj);
        System.out.println("Before format -> " + localDateTime);
        System.out.println("After format -> " + formattedDate);

        LocalTime localTime1 = localDateTime.toLocalTime();
        System.out.println(localTime1);
        System.out.println(localDateTime);

        System.out.println("---------------------------");

        String localDateTimeString = localDateTime.toString();
        System.out.println("Before format ->" + localDateTimeString);
        String formatLD = localDateTime.format(myFormatObj);
        System.out.println("After format -> " + formatLD);

        System.out.println(" ");

        LocalDateTime localDateTimeFromString = LocalDateTime.parse("2019-10-22T22:38");
        System.out.println("LDT from string -> " + localDateTimeFromString);

        System.out.println("--------------FINAL SOLUTION-------------");

        LocalDate customerLocalDate = LocalDate.parse("2019-10-22");
        LocalTime timeNow = LocalTime.now();

        LocalDateTime customerFutureDate = LocalDateTime.of(customerLocalDate, timeNow);

        System.out.println("LocalDateTime from 2 values -> " + customerFutureDate);

        System.out.println("--------------COMPARE DATE-------------");

        System.out.println(customerFutureDate.compareTo(LocalDateTime.now()));

        Duration duration = Duration.between(LocalDateTime.now(), localDateTimeFromString);
        System.out.println(duration);

        LocalDateTime aLDT = LocalDateTime.now();
        LocalDateTime bLDT = localDateTimeFromString;

        long diff = ChronoUnit.SECONDS.between(aLDT, bLDT);
        System.out.println(diff);

        long s = diff;
        long numberOfDays = s / 86400;
        long numberOfHours = (s % 86400 ) / 3600 ;
        long numberOfMinutes = ((s % 86400 ) % 3600 ) / 60;
        long numberOfSeconds = ((s % 86400 ) % 3600 ) % 60;
        System.out.println(aLDT + "\n" + bLDT);
        System.out.println(numberOfDays + ":" + numberOfHours + ":" + numberOfMinutes + ":" + numberOfSeconds);

    }

}
