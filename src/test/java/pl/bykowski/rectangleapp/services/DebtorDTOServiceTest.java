package pl.bykowski.rectangleapp.services;

import junitparams.JUnitParamsRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.model.dto.DebtorDTO;
import pl.bykowski.rectangleapp.repositories.DebtorDetailsRepo;
import pl.bykowski.rectangleapp.repositories.DebtorHistoryRepo;
import pl.bykowski.rectangleapp.services.tdo_services.DebtorDTOService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(JUnitParamsRunner.class)
public class DebtorDTOServiceTest {

    private DebtorDTOService debtorDTOService;
    private DebtorDetailsService debtorDetailsService;
    private DebtorService debtorService;
    private Principal principal;

    @Before
    public void init(){
        principal = mock(Principal.class);

        DebtorDetailsRepo debtorDetailsRepo = mock(DebtorDetailsRepo.class);
        DebtorHistoryRepo debtorHistoryRepo = mock(DebtorHistoryRepo.class);
        DebtorHistoryService debtorHistoryService = new DebtorHistoryService(debtorHistoryRepo);
        debtorDetailsService = new DebtorDetailsService(debtorDetailsRepo, debtorHistoryService);

        debtorService = mock(DebtorService.class);
        debtorDTOService = new DebtorDTOService(debtorDetailsService, debtorService);
    }

    @Test
    public void should_return_debtorDTOList_based_on_debtorList() {
        //given
        String debtor1Name = "Ada";
        Long debtor1Id = 1L;
        BigDecimal debtor1Debt = new BigDecimal(10);
        Debtor debtor1 = new Debtor();
        debtor1.setName(debtor1Name);
        debtor1.setId(debtor1Id);
        debtor1.setTotalDebt(debtor1Debt);
        String debtor2Name = "Artur";
        Debtor debtor2 = new Debtor();
        debtor2.setName(debtor2Name);
        List<Debtor> debtorList = Arrays.asList(debtor1, debtor2);
        //when
        List<DebtorDTO> found = debtorDTOService.returnDebtorDTOList(debtorList);
        //then
        assertThat(found.size()).isEqualTo(debtorList.size());
        assertThat(debtor1.getName()).isEqualTo(debtorList.get(0).getName());
        assertThat(debtor1.getId()).isEqualTo(debtorList.get(0).getId());
        assertThat(debtor1.getTotalDebt()).isEqualTo(debtorList.get(0).getTotalDebt());
    }

    @Test
    public void should_return_debtor_with_highest_count_of_debts() {
        //given
        Long debtorDetails1Id = 5L;
        Long debtorDetails2Id = 6L;
        Long debtorDetails3Id = 7L;

        String principalName = "Adrian";
        Debtor debtor1 = new Debtor();
        Long idDebtor1 = 1L;
        debtor1.setId(idDebtor1);

        Debtor debtor2 = new Debtor();
        Long idDebtor2 = 2L;
        debtor2.setId(idDebtor2);

        given(principal.getName()).willReturn(principalName);
        DebtorDetails debtorDetails1 = new DebtorDetails();
        debtorDetails1.setDebtor(debtor1);
        debtorDetails1.setId(debtorDetails1Id);

        DebtorDetails debtorDetails2 = new DebtorDetails();
        debtorDetails2.setDebtor(debtor1);
        debtorDetails2.setId(debtorDetails2Id);

        DebtorDetails debtorDetails3 = new DebtorDetails();
        debtorDetails3.setDebtor(debtor2);
        debtorDetails3.setId(debtorDetails3Id);

        ArrayList<DebtorDetails> debtorDetailsArrayList = new ArrayList<>();
        debtorDetailsArrayList.add(debtorDetails1);
        debtorDetailsArrayList.add(debtorDetails3);
        debtorDetailsArrayList.add(debtorDetails2);
        given(debtorDetailsService.findByUserName(principal.getName())).willReturn(debtorDetailsArrayList);
        given(debtorService.findById(debtor1.getId())).willReturn(java.util.Optional.of(debtor1));
        //when
        DebtorDTO found = debtorDTOService.returnDebtorDTOWithHighestCountOfDebts(principal);
        //then
        assertThat(found.getId()).isEqualTo(debtor1.getId());
    }

}
