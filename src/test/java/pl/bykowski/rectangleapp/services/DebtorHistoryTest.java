package pl.bykowski.rectangleapp.services;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(JUnitParamsRunner.class)
public class DebtorHistoryTest {

    private DebtorHistoryService debtorHistoryService;
    private DebtorHistoryRepo debtorHistoryRepo;

    @Before
    public void init(){
        debtorHistoryRepo = mock(DebtorHistoryRepo.class);
        debtorHistoryService = new DebtorHistoryService(debtorHistoryRepo);
    }

    @Test
    @Parameters({"Adrian", "adrian", "1234", "!@#$"})
    public void should_return_debtorHistory_list_by_userName(String userNameParam) {
        //given
        DebtorHistory debtorHistory1 = new DebtorHistory();
        DebtorHistory debtorHistory2 = new DebtorHistory();
        DebtorHistory debtorHistory3 = new DebtorHistory();
        List<DebtorHistory> debtorHistoryList = Arrays.asList(debtorHistory1, debtorHistory2, debtorHistory3);
        given(debtorHistoryRepo.findByUserName(userNameParam)).willReturn(debtorHistoryList);
        //when
        List<DebtorHistory> foundList = debtorHistoryService.findByUserName(userNameParam);
        //then
        assertThat(foundList.size()).isEqualTo(debtorHistoryList.size());
    }

    @Test
    public void should_make_and_save_new_debtorHistory_based_on_debtorDetails() {
        //given
        BigDecimal debtValue = new BigDecimal(10);
        String name = "Ada";
        String reasonForTheDebt = "coffee";
        String userName = "Adrian";
        LocalDate debtDate = LocalDate.of(2019, Month.FEBRUARY, 2);

//        DebtorDetails debtorDetails = new DebtorDetails();
//        debtorDetails.setDebt(debtValue);
//        debtorDetails.setName(name);
//        debtorDetails.setReasonForTheDebt(reasonForTheDebt);
//        debtorDetails.setUserName(userName);
//        debtorDetails.setDate(debtDate);
        DebtorDetails debtorDetails = DebtorDetails.builder()
                .debt(debtValue)
                .name(name)
                .reasonForTheDebt(reasonForTheDebt)
                .userName(userName)
                .date(debtDate)
                .build();

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
