package pl.bykowski.rectangleapp.services;

import junitparams.JUnitParamsRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.model.dto.DebtorDetailsDTO;
import pl.bykowski.rectangleapp.repositories.DebtorRepo;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(JUnitParamsRunner.class)
public class DebtorServiceTest {

    private DebtorService debtorService;
    private DebtorRepo debtorRepo;
    private UserService userService;
    private DebtorDetailsService debtorDetailsService;
    private DebtorHistoryService debtorHistoryService;
    private Principal principal;

    @Before
    public void init() {
        debtorRepo = mock(DebtorRepo.class);
        userService = mock(UserService.class);
        debtorDetailsService = mock(DebtorDetailsService.class);
        debtorHistoryService = mock(DebtorHistoryService.class);
        principal = mock(Principal.class);

        debtorService = new DebtorService(debtorRepo, userService, debtorDetailsService, debtorHistoryService);
    }

    @Test
    public void should_return_debtor_with_correct_name() {
        //given
        String debtorName = "Adrian";
        Debtor debtor = new Debtor();
        debtor.setName(debtorName);

        given(debtorRepo.findByName(debtorName))
                .willReturn(java.util.Optional.of(debtor));
        //when
        Optional<Debtor> found = debtorService.findDebtorByName(debtorName);
        //then
        assertThat(found.get().getName()).isEqualTo(debtor.getName());
    }

    @Test
    public void should_add_new_debtor_if_name_was_never_used() {
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

        Debtor debtor = Debtor.builder()
                .name(debtorName)
                .totalDebt(debtValue)
                .userName(userName)
                .build();

        DebtorDetailsDTO debtorDetailsDTO = new DebtorDetailsDTO();
        debtorDetailsDTO.setName(debtor.getName());
        debtorDetailsDTO.setDebt(debtor.getTotalDebt());
        debtorDetailsDTO.setReasonForTheDebt(reasonForTheDebt);
        //when
        debtorService.addNewDebtor(debtorDetailsDTO, userName);
        //then
        verify(debtorDetailsService).addNewDebtorDetails(debtorDetailsDTO, userName, debtor);
    }

    @Test
    public void should_return_debtor_with_biggest_debt() {
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

        given(principal.getName()).willReturn("Adrian");
        given(debtorRepo.findByUserName(principal.getName())).willReturn(debtorList);
        //when
        Optional<Debtor> found = debtorService.returnDebtorWithBiggestDebt(principal.getName());
        //then
        assertThat(debtorWithBiggestDebt.getName()).isEqualTo(found.get().getName());
    }

    @Test
    public void should_return_new_total_debt() {
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
        BigDecimal actualDebt = debtorService.updateTotalDebt(id, additionalDebt);
        //then
        assertThat(actualDebt).isEqualTo(new BigDecimal(20));
    }

    @Test
    public void should_update_total_debt_and_make_new_debtor_details() {
        //given
        String userName = "Adrian";
        String debtorDetailsName = "Ada";
        BigDecimal debtValue = new BigDecimal(10);
        String reasonForDebt = "ReasonForDebt";

        DebtorDetailsDTO debtorDetailsDTO = new DebtorDetailsDTO();
        debtorDetailsDTO.setName(debtorDetailsName);
        debtorDetailsDTO.setDebt(debtValue);
        debtorDetailsDTO.setReasonForTheDebt(reasonForDebt);

        Long debtorId = 1L;
        Debtor debtor = new Debtor();
        debtor.setId(debtorId);
        //when
        debtorService.updateTotalDebtAndMakeNewDebtorDetails(debtorDetailsDTO, debtor, userName);
        //then
        verify(debtorDetailsService).addNewDebtorDetails(debtorDetailsDTO, userName, debtor);
    }

    @Test
    public void delete_debtorDetails_update_total_debt_make_new_debtor_history() {
        //given
        Long id = 1L;
        Debtor debtor = new Debtor();
        debtor.setId(1L);
        DebtorDetails debtorDetails = DebtorDetails.builder()
                .debtor(debtor)
                .build();
        given(debtorDetailsService.findById(id)).willReturn(Optional.of(debtorDetails));
        //when
        debtorService.deleteDebtorDetailsUpdateTotalDebtMakeNewDebtorHistory(id);
        //then
        verify(debtorHistoryService).saveEntityDebtorHistory(debtorDetails);
        verify(debtorDetailsService).deleteById(id);
    }

    @Test
    public void should_update_debt_ind_debtorDetails_and_in_debtor() {
        //given
        Long debtorDetailsId = 1L;
        BigDecimal debtorDetailsDTODebt = new BigDecimal(10);

        DebtorDetailsDTO debtorDetailsDTO = new DebtorDetailsDTO();
        debtorDetailsDTO.setDebt(debtorDetailsDTODebt);
        //when
        debtorService.updateTotalDebtAndUpdateDebtorDetailsDebt(debtorDetailsDTO, debtorDetailsId);
        //then
        verify(debtorDetailsService).updateDebtorDetailsDebt(debtorDetailsId, debtorDetailsDTO.getDebt());
    }
}
