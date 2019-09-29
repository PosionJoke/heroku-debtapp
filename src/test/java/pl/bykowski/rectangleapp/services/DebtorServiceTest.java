package pl.bykowski.rectangleapp.services;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.model.dto.DebtorDetailsDTO;
import pl.bykowski.rectangleapp.repositories.DebtorRepo;

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

    @InjectMocks
    private DebtorService debtorService;
    @Mock
    private DebtorRepo debtorRepo;
    @Mock
    private UserService userService;
    @Mock
    private DebtorDetailsService debtorDetailsService;
    @Mock
    private DebtorHistoryService debtorHistoryService;


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
        debtor.setTotalDebt(debtValue);
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
        verify(debtorDetailsService).addNewDebtorDetails(debtorDetailsDTO.getName(),
                debtorDetailsDTO.getDebt(),
                debtorDetailsDTO.getReasonForTheDebt(),
                userName,
                debtor);
    }

    @Test
    public void delete_debtorDetails_update_total_debt_make_new_debtor_history() {
        //given
        Long id = 1L;
        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "Adrian";
            }
        };
        DebtorDetails debtorDetails = new DebtorDetails();
        given(debtorDetailsService.findById(id)).willReturn(Optional.of(debtorDetails));
        //when
        debtorService.deleteDebtorDetailsUpdateTotalDebtMakeNewDebtorHistory(id, principal);
        //then
        verify(debtorHistoryService).saveEntityDebtorHistory(debtorDetails);
        verify(debtorDetailsService).deleteById(id);
    }

    @Test
    public void should_update_debt_ind_debtorDetails_and_in_debtor() {
        //given
        Long debtorDetailsId = 1L;
        String debtorDetailsUserName = "Adrian";
        BigDecimal debtorDetailsDTODebt = new BigDecimal(10);

        DebtorDetailsDTO debtorDetailsDTO = new DebtorDetailsDTO();
        debtorDetailsDTO.setDebt(debtorDetailsDTODebt);

        DebtorDetails debtorDetails = new DebtorDetails();
        debtorDetails.setId(debtorDetailsId);
        debtorDetails.setUserName(debtorDetailsUserName);
        //when
        debtorService.updateTotalDebtAndUpdateDebtorDetailsDebt(debtorDetailsDTO, debtorDetailsId);
        //then
        verify(debtorDetailsService).updateDebtorDetailsDebt(debtorDetailsId, debtorDetailsDTO.getDebt());
    }
}
