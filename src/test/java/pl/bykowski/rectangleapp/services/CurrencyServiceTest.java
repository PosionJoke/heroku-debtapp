package pl.bykowski.rectangleapp.services;

import junitparams.JUnitParamsRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.web.client.RestTemplateBuilder;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.dto.DebtorDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(JUnitParamsRunner.class)
public class CurrencyServiceTest {

    private CurrencyService currencyService;

    @Before
    public void init(){
        this.currencyService = new CurrencyService(new RestTemplateBuilder());
        Debtor debtor1 = new Debtor();
        debtor1.setTotalDebt(new BigDecimal(10));
        Debtor debtor2 = new Debtor();
        debtor2.setTotalDebt(new BigDecimal(100));
    }

    @Test
    public void should_set_new_total_debt_based_on_currencyRates(){
        //given
        String currencyRate = "2";
        DebtorDTO debtorDTO1 = new DebtorDTO();
        debtorDTO1.setTotalDebt(new BigDecimal(10));
        DebtorDTO debtorDTO2 = new DebtorDTO();
        debtorDTO2.setTotalDebt(new BigDecimal(100));
        List<DebtorDTO> debtorDTOList = Arrays.asList(debtorDTO1, debtorDTO2);
        //when
        List<DebtorDTO> found = currencyService.setCurrencyRates(debtorDTOList, currencyRate);
        //then
        assertThat(found.get(0).getTotalDebt()).isEqualTo(new BigDecimal("5.000"));
        assertThat(found.get(1).getTotalDebt()).isEqualTo(new BigDecimal("50.000"));
    }

    public static void main(String[] args) {
        System.out.println(

                new BigDecimal("20").divide(new BigDecimal("2"))

        );

        Debtor debtor = new Debtor();
        debtor.setTotalDebt(new BigDecimal("10"));
        Debtor debtor1 = new Debtor();
        debtor1.setTotalDebt(new BigDecimal("100"));
        List<Debtor> debtorList = Arrays.asList(debtor, debtor1);

        debtorList.forEach(n -> {
            n.setTotalDebt(n.getTotalDebt().multiply(new BigDecimal(0.5)));
        });

        debtorList.forEach(n -> System.out.println("-> " + n));

        System.out.println(new BigDecimal(2).divide(new BigDecimal(4)));
        System.out.println(new BigDecimal(2).divide(new BigDecimal(0.5)));
        System.out.println(new BigDecimal("3.8"));
        System.out.println(new BigDecimal("1").divide(new BigDecimal("3.8"), 3, RoundingMode.CEILING));

    }
}
