package pl.bykowski.rectangleapp.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyService {

    private RestTemplate restTemplate;

    public CurrencyService(){
        this.restTemplate = new RestTemplate();
    }

    public String calculateCurrencyRates(String currencyOne, String currencyTwo){
        JsonNode jsonNode = restTemplate.getForObject("https://api.exchangerate-api.com/v4/latest/" + currencyOne.toUpperCase(), JsonNode.class).get("rates")
                .get(currencyTwo.toUpperCase());

        return  jsonNode.asText();
    }
}
