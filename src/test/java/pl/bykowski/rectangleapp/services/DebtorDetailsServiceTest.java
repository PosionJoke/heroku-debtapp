package pl.bykowski.rectangleapp.services;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.model.dto.DebtorDetailsDTO;
import pl.bykowski.rectangleapp.repositories.DebtorDetailsRepo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(JUnitParamsRunner.class)
public class DebtorDetailsServiceTest {

    private DebtorDetailsService debtorDetailsService;
    private DebtorDetailsRepo debtorDetailsRepo;
    private DebtorHistoryService debtorHistoryService = mock(DebtorHistoryService.class);

    private String debtorName;
    private Long debtorId;
    private BigDecimal debtValue;
    private String reasonForDebt;
    private String userName;
    private Debtor debtor;
    private DebtorDetails debtorDetails;


    @Before
    public void init() {
        debtorDetailsRepo = mock(DebtorDetailsRepo.class);

        debtorDetailsService = new DebtorDetailsService(debtorDetailsRepo, debtorHistoryService);

        debtorName = "Ada";
        debtValue = new BigDecimal(10);
        reasonForDebt = "coffee";
        userName = "Adrian";
        debtor = new Debtor();
        debtorId = 3L;

        debtorDetails = DebtorDetails.builder()
                .name(debtorName)
                .debt(debtValue)
                .reasonForTheDebt(reasonForDebt)
                .userName(userName)
                .debtor(debtor)
                .id(debtorId)
                .build();
    }

    @Test
    public void should_save_new_DebtorDetails() {
        //given
        DebtorDetails debtorDetailsTest = DebtorDetails.builder()
                .userName(userName)
                .name(debtorName)
                .debt(debtValue)
                .reasonForTheDebt(reasonForDebt)
                .debtor(debtor)
                .build();

        DebtorDetailsDTO debtorDetailsDTO = new DebtorDetailsDTO();
        debtorDetailsDTO.setName(debtorDetailsTest.getName());
        debtorDetailsDTO.setDebt(debtorDetailsTest.getDebt());
        debtorDetailsDTO.setReasonForTheDebt(debtorDetailsTest.getReasonForTheDebt());
        //when
        DebtorDetails created = debtorDetailsService.addNewDebtorDetails(debtorDetailsDTO, userName, debtor);
        //then
        assertThat(created).isEqualTo(debtorDetailsTest);
    }

    @Test
    public void should_delete_debt_by_Id() {
        //given
        DebtorDetails debtorDetailsFoundById = DebtorDetails.builder()
                .id(1L)
                .name("Alex")
                .reasonForTheDebt("Coffee")
                .userName("Adrian")
                .debtor(new Debtor())
                .build();
        given(debtorDetailsRepo.findById(debtorId)).willReturn(Optional.of(debtorDetailsFoundById));
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
    @Parameters({"-11", "-10"})
    public void should_delete_debt_when_the_debt_value_is_under_zero(Integer value) {
        //given
        BigDecimal negativeDebt = new BigDecimal(value);
        given(debtorDetailsRepo.findById(debtorId)).willReturn(java.util.Optional.ofNullable(debtorDetails));
        //when
        debtorDetailsService.updateDebtorDetailsDebt(debtorId, negativeDebt);
        //then
        verify(debtorDetailsRepo).delete(debtorDetails);
    }

    @Test
    @Parameters({"Adrian", "adrian", "adrian1234", "1234"})
    public void should_return_DebtorDetailsList_by_user_name(String debtorName) {
        //given
        DebtorDetails debtorDetails1 = DebtorDetails.builder()
                .userName(debtorName)
                .build();
        DebtorDetails debtorDetails2 = DebtorDetails.builder()
                .userName(debtorName)
                .build();
        DebtorDetails debtorDetails3 = DebtorDetails.builder()
                .userName(debtorName)
                .build();
        List<DebtorDetails> debtorDetailsList = Arrays.asList(debtorDetails1, debtorDetails2, debtorDetails3);
        given(debtorDetailsRepo.findByUserName(debtorName)).willReturn(debtorDetailsList);
        //when
        List<DebtorDetails> found = debtorDetailsService.findByUserName(debtorName);
        //then
        assertThat(found).isEqualTo(debtorDetailsList);
    }

    @Test
    @Parameters({"1", "0", "-1"})
    public void should_return_debtorDetails_by_id(Long debtorId) {
        //given
        DebtorDetails debtorDetails = DebtorDetails.builder()
                .id(debtorId)
                .build();
        given(debtorDetailsRepo.findById(debtorId)).willReturn(java.util.Optional.of(debtorDetails));
        //when
        debtorDetailsService.findById(debtorId);
        //then
        assertThat(debtorDetails.getId()).isEqualTo(debtorId);
    }

    @Test
    @Parameters({"1", "0", "-1"})
    public void should_delete_debtor_by_id(Long debtorId) {
        //given
        //when
        debtorDetailsService.deleteById(debtorId);
        //then
        verify(debtorDetailsRepo).deleteById(debtorId);
    }
}
