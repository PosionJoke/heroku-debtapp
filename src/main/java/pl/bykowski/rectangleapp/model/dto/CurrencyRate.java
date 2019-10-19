package pl.bykowski.rectangleapp.model.dto;

import java.math.BigDecimal;

public interface CurrencyRate {

    BigDecimal getDebt();
    void setDebt(BigDecimal newDebt);

}
