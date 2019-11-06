package pl.bykowski.rectangleapp.services;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.log4j.Log4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.bykowski.rectangleapp.error_handler.RestTemplateErrorHandler;
import pl.bykowski.rectangleapp.model.CurrencyRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Log4j
@Service
public class CurrencyService {

    private static final String httpCurrentRate = "https://api.exchangerate-api.com/v4/latest/";
    private RestTemplate restTemplate;

    public CurrencyService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateErrorHandler())
                .build();
    }

    public String calculateCurrencyRates(String currencyOne, String currencyTwo) {

        if (currencyOne == null) {
            currencyOne = "PLN";
        }

        JsonNode jsonNode;

        try {
            jsonNode = restTemplate.getForObject(httpCurrentRate +
                    currencyOne.toUpperCase(), JsonNode.class).get("rates")
                    .get(currencyTwo.toUpperCase());
            return jsonNode.asText();
        } catch (Exception ex) {
            log.debug(String.format("Cannot connect to [%s]", httpCurrentRate));
            return "1";
        }
    }


    public <T extends CurrencyRate> List<T> setCurrencyRates(List<T> list, String currencyRate) {
        list
                .forEach(listStream -> listStream.setDebt(
                        listStream.getDebt().divide(new BigDecimal(currencyRate), 2, RoundingMode.CEILING)
                ));
        return list;
    }
}
