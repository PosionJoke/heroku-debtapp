package pl.bykowski.rectangleapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.model.DebtorHistory;
import pl.bykowski.rectangleapp.repositories.DebtorHistoryRepo;
import pl.bykowski.rectangleapp.services.DebtorHistoryService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class DebtorHistoryTest {

    @InjectMocks
    private DebtorHistoryService debtorHistoryService;
    @Mock
    private DebtorHistoryRepo debtorHistoryRepo;

    @Test
    public void should_return_debtorHistory_list_by_userName() {
        //given
        String userName = "Adrian";
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
