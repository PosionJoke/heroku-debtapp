package pl.bykowski.rectangleapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.bykowski.rectangleapp.model.DebtorHistory;
import pl.bykowski.rectangleapp.repositories.DebtorHistoryRepo;
import pl.bykowski.rectangleapp.services.DebtorHistoryService;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class DebtorHistoryTest {

    @InjectMocks
    private DebtorHistoryService debtorHistoryService;
    @Mock
    private DebtorHistoryRepo debtorHistoryRepo;

    @Test
    public void should_return_debtorDetails_list_by_userName(){
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

}
