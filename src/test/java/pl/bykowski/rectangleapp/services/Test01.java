package pl.bykowski.rectangleapp.services;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorHistory;
import pl.bykowski.rectangleapp.repositories.DebtorHistoryRepo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.BDDMockito.given;

@RunWith(JUnitParamsRunner.class)
public class Test01 {

    private DebtorHistoryService debtorHistoryService;

    @MockBean
    private DebtorHistoryRepo debtorHistoryRepo;

    @Before
    public void init(){
        debtorHistoryService = new DebtorHistoryService(debtorHistoryRepo);
    }
    
    @Test
    @Parameters({"17, false",
            "22, true" })
    public void personIsAdult(String age, boolean valid) throws Exception {

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
