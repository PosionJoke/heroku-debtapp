package pl.bykowski.rectangleapp;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.repositories.DebtorRepo;
import pl.bykowski.rectangleapp.services.DebtorDetailsService;
import pl.bykowski.rectangleapp.services.DebtorHistoryService;
import pl.bykowski.rectangleapp.services.DebtorService;
import pl.bykowski.rectangleapp.services.UserService;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class DebtorServiceTest {

    @Mock
    DebtorRepo debtorRepo;
    @Mock
    UserService userService;
    @Mock
    DebtorDetailsService debtorDetailsService;
    @Mock
    DebtorHistoryService debtorHistoryService;

    @InjectMocks
    DebtorService debtorService;



    @Test
    public void should_return_a(){
        assertThat(debtorService.testReturnA()).isEqualTo("A");
    }

    @Test
    public void name_to_change(){
        //given
        String debtorName = "Adrian";

        Debtor debtor = new Debtor();
        debtor.setName(debtorName);

        given(debtorRepo.findByName(debtorName))
                .willReturn(java.util.Optional.of(debtor));
        //when
        Debtor found = debtorService.findDebtorByName(debtorName);
        //then
        assertThat(found.getName()).isEqualTo(debtor.getName());
    }

    @Test
    public void should_add_new_debtor_if_name_was_never_used(){
        //given
        String debtorName = "Michal";
        BigDecimal debtValue = new BigDecimal(10);
        String reasonForTheDebt = "Coffee";
        String userName = "Adrian";
        given(userService.findUserName()).willReturn(userName);

        Debtor fakeDebtor1 = new Debtor();
        fakeDebtor1.setName("Aga");
        Debtor fakeDebtor2 = new Debtor();
        fakeDebtor2.setName("Artur");
        List<Debtor> debtorList = Arrays.asList(fakeDebtor1, fakeDebtor2);
        given(debtorRepo.findByUserName(userName)).willReturn(debtorList);

        Debtor debtor = new Debtor();
        debtor.setName(debtorName);
        debtor.setTotalDebt(new BigDecimal(0));
        debtor.setDateOfJoining(LocalDate.now());
        debtor.setUserName(userName);

        //when
        debtorService.addNewDebtor(debtorName, debtValue, reasonForTheDebt, userName);

        //then
        verify(debtorDetailsService).addNewDebtorDetails(debtorName, debtValue, reasonForTheDebt, userName, debtor);
    }

    @Test
    public void should_return_debtor_with_biggest_debt(){
        //given
        Debtor debtorWithBiggestDebt = new Debtor();
        debtorWithBiggestDebt.setName("Michal");
        debtorWithBiggestDebt.setDebt(new BigDecimal(1000));

        Debtor debtorWithMediumDebt = new Debtor();
        debtorWithMediumDebt.setName("Aga");
        debtorWithMediumDebt.setDebt(new BigDecimal(100));

        Debtor debtorWithLowestDebt = new Debtor();
        debtorWithLowestDebt.setName("Artur");
        debtorWithLowestDebt.setDebt(new BigDecimal(10));

        List<Debtor> debtorList = Arrays.asList(debtorWithBiggestDebt, debtorWithMediumDebt, debtorWithLowestDebt);

        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "Adrian";
            }
        };

        given(debtorRepo.findByUserName(principal.getName())).willReturn(debtorList);
        //when
        Optional<Debtor> found = debtorService.returnDebtorWithBiggestDebt(principal);
        //then
        assertThat(debtorWithBiggestDebt.getName()).isEqualTo(found.get().getName());
    }

    @Test
    public void should_return_new_total_debt(){
        //given
        Long id = 1L;
        BigDecimal additionalDebt = new BigDecimal(10);
        String userName = "Adrian";
        Debtor debtor = new Debtor();
        debtor.setName("Ada");
        debtor.setTotalDebt(new BigDecimal(10));
        debtor.setUserName(userName);
        given(debtorRepo.findById(id)).willReturn(Optional.of(debtor));
        //when
        BigDecimal actualDebt = debtorService.updateTotalDebt(id, additionalDebt, userName);
        //then
        assertThat(actualDebt).isEqualTo(new BigDecimal(20));
    }
}
