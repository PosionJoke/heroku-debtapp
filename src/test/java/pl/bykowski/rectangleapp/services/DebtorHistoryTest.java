package pl.bykowski.rectangleapp.services;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.model.DebtorHistory;
import pl.bykowski.rectangleapp.repositories.DebtorHistoryRepo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(JUnitParamsRunner.class)
public class DebtorHistoryTest {

    private DebtorHistoryService debtorHistoryService;

    private DebtorHistoryRepo debtorHistoryRepo;

    @Before
    public void init(){
        debtorHistoryRepo = Mockito.mock(DebtorHistoryRepo.class);
        debtorHistoryService = new DebtorHistoryService(debtorHistoryRepo);
    }

    @Test
    @Parameters({"Adrian", "adrian", "1234", "!@#$"})
    public void should_return_debtorHistory_list_by_userName(String userNameParam) {
        //given
        String userName = userNameParam;
        DebtorHistory debtorHistory1 = new DebtorHistory();
        DebtorHistory debtorHistory2 = new DebtorHistory();
        DebtorHistory debtorHistory3 = new DebtorHistory();
        List<DebtorHistory> debtorHistoryList = Arrays.asList(debtorHistory1, debtorHistory2, debtorHistory3);
        given(debtorHistoryRepo.findByUserName(userName)).willReturn(debtorHistoryList);
        //when
        List<DebtorHistory> foundList = debtorHistoryService.findByUserName(userName);
        //then
        assertThat(foundList.size()).isEqualTo(debtorHistoryList.size());
    }

    @Test
    public void should_make_and_save_new_debtorHistory_based_on_debtorDetails() {
        //given
        DebtorDetails debtorDetails = new DebtorDetails();
        BigDecimal debtValue = new BigDecimal(10);
        debtorDetails.setDebt(debtValue);
        String name = "Ada";
        debtorDetails.setName(name);
        String reasonForTheDebt = "coffee";
        debtorDetails.setReasonForTheDebt(reasonForTheDebt);
        String userName = "Adrian";
        debtorDetails.setUserName(userName);
        LocalDate debtDate = LocalDate.of(2019, Month.FEBRUARY, 2);
        debtorDetails.setDate(debtDate);

        DebtorHistory debtorHistory = new DebtorHistory();
        debtorHistory.setDebt(debtorDetails.getDebt());
        debtorHistory.setName(debtorDetails.getName());
        debtorHistory.setReasonForTheDebt(debtorDetails.getReasonForTheDebt());
        debtorHistory.setUserName(debtorDetails.getUserName());
        long daysBetween = DAYS.between(debtorDetails.getDate(), LocalDate.now());
        debtorHistory.setTimeOfDebt(daysBetween);

        //when
        debtorHistoryService.saveEntityDebtorHistory(debtorDetails);
        //then
        verify(debtorHistoryRepo).save(debtorHistory);
    }

}
