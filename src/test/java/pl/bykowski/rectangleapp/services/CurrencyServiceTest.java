package pl.bykowski.rectangleapp.services;

import junitparams.JUnitParamsRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.bykowski.rectangleapp.model.Debtor;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(JUnitParamsRunner.class)
public class CurrencyServiceTest {

    private CurrencyService currencyService;
    private List<Debtor> debtorList;

    @Before
    public void init(){
        this.currencyService = new CurrencyService();
        Debtor debtor1 = new Debtor();
        debtor1.setTotalDebt(new BigDecimal(10));
        Debtor debtor2 = new Debtor();
        debtor2.setTotalDebt(new BigDecimal(100));
        debtorList = Arrays.asList(debtor1, debtor2);
    }

    @Test
    public void should_set_new_total_debt_based_on_currencyRates(){
        //given
        String currencyRate = "2";
        //when
        List<Debtor> found = currencyService.setCurrencyRateForDebtors(debtorList, currencyRate);
        //then
        assertThat(found.get(0).getTotalDebt()).isEqualTo(new BigDecimal("5"));
        assertThat(found.get(1).getTotalDebt()).isEqualTo(new BigDecimal("50"));
    }
//TODO googl why we cant divide as BigDecimal 1 and 3.8
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
        System.out.println(new BigDecimal("1").divide(new BigDecimal("3.8")));

    }
}
