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
}
