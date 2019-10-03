package pl.bykowski.rectangleapp.services;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorHistory;
import pl.bykowski.rectangleapp.repositories.DebtorHistoryRepo;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(JUnitParamsRunner.class)
public class Test01 {

    private DebtorHistoryService debtorHistoryService;

    private DebtorHistoryRepo debtorHistoryRepo;

    @Before
    public void init(){
        debtorHistoryRepo = mock(DebtorHistoryRepo.class);
        debtorHistoryService = new DebtorHistoryService(debtorHistoryRepo);
    }
    
    @Test
    @Parameters({"22", "17"})
    public void personIsAdult(String age) throws Exception {

        Debtor debtor = new Debtor();
        String debtorName = "22";
        debtor.setName(debtorName);

        assertThat(debtor.getName()).isEqualTo(age);
    }

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
        Assertions.assertThat(foundList.size()).isEqualTo(debtorHistoryList.size());
    }

}
