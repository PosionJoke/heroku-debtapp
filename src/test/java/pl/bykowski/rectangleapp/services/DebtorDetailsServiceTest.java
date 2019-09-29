package pl.bykowski.rectangleapp.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.repositories.DebtorDetailsRepo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class DebtorDetailsServiceTest {

    @InjectMocks
    private DebtorDetailsService debtorDetailsService;
    @Mock
    private DebtorDetailsRepo debtorDetailsRepo;
    @Mock
    private DebtorHistoryService debtorHistoryService;

    private String debtorName;
    private Long debtorId;
    private BigDecimal debtValue;
    private String reasonForDebt;
    private String userName;
    private Debtor debtor;
    private DebtorDetails debtorDetails = new DebtorDetails();

    @Before
    public void init() {
        debtorName = "Ada";
        debtValue = new BigDecimal(10);
        reasonForDebt = "coffee";
        userName = "Adrian";
        debtor = new Debtor();
        debtorId = 3L;

        debtorDetails.setName(debtorName);
        debtorDetails.setDebt(debtValue);
        debtorDetails.setDate(LocalDate.now());
        debtorDetails.setReasonForTheDebt(reasonForDebt);
        debtorDetails.setUserName(userName);
        debtorDetails.setDebtor(debtor);
        debtorDetails.setId(debtorId);
    }

    @Test
    public void should_save_new_DebtorDetails() {
        //given
        //when
        DebtorDetails created = debtorDetailsService.addNewDebtorDetails(debtorName, debtValue, reasonForDebt, userName, debtor);
        //then
        assertThat(created).isEqualTo(debtorDetails);
    }

    @Test
    public void should_delete_debt_by_Id() {
        //given
        DebtorDetails debtorDetailsFoundById = new DebtorDetails();
        given(debtorDetailsRepo.findById(debtorId)).willReturn(java.util.Optional.of(debtorDetailsFoundById));
        //when
        debtorDetailsService.deleteDebtById(debtorId);
        //then
        verify(debtorHistoryService).saveEntityDebtorHistory(debtorDetailsFoundById);
        verify(debtorDetailsRepo).delete(debtorDetailsFoundById);
    }

    @Test
    public void should_update_DebtorDetails_debt() {
        //given
        given(debtorDetailsRepo.findById(debtorId)).willReturn(java.util.Optional.ofNullable(debtorDetails));
        //when
        debtorDetailsService.updateDebtorDetailsDebt(debtorId, debtValue);
        //then
        assertThat(debtorDetails.getDebt()).isEqualTo(new BigDecimal(20));
    }

    @Test
    public void should_delete_debt_when_the_debt_value_is_under_zero() {
        //given
        BigDecimal negativeDebt = new BigDecimal(-20);
        given(debtorDetailsRepo.findById(debtorId)).willReturn(java.util.Optional.ofNullable(debtorDetails));
        //when
        debtorDetailsService.updateDebtorDetailsDebt(debtorId, negativeDebt);
        //then
        verify(debtorHistoryService).saveEntityDebtorHistory(debtorDetails);
        verify(debtorDetailsRepo).delete(debtorDetails);
    }

    @Test
    public void should_return_DebtorDetailsList_by_user_name() {
        //given
        DebtorDetails debtorDetails1 = new DebtorDetails();
        debtorDetails1.setUserName(userName);
        DebtorDetails debtorDetails2 = new DebtorDetails();
        debtorDetails2.setUserName(userName);
        DebtorDetails debtorDetails3 = new DebtorDetails();
        debtorDetails3.setUserName(userName);
        List<DebtorDetails> debtorDetailsList = Arrays.asList(debtorDetails1, debtorDetails2, debtorDetails3);
        given(debtorDetailsRepo.findByUserName(userName)).willReturn(debtorDetailsList);
        //when
        List<DebtorDetails> found = debtorDetailsService.findByUserName(userName);
        //then
        assertThat(found).isEqualTo(debtorDetailsList);
    }

    //TODO name of method should show what that test check, not show name of testing method
    @Test
    public void should_return_debtorDetails_by_id() {
        //given
        DebtorDetails debtorDetails = new DebtorDetails();
        debtorDetails.setId(debtorId);
        given(debtorDetailsRepo.findById(debtorId)).willReturn(java.util.Optional.of(debtorDetails));
        //when
        debtorDetailsService.findById(debtorId);
        //then
        assertThat(debtorDetails.getId()).isEqualTo(debtorId);
    }

    @Test
    public void should_delete_debtor_by_id() {
        //given
        //when
        debtorDetailsService.deleteById(debtorId);
        //then
        verify(debtorDetailsRepo).deleteById(debtorId);
    }

}
