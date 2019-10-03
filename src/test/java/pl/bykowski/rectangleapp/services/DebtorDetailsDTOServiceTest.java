package pl.bykowski.rectangleapp.services;

import junitparams.JUnitParamsRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.model.dto.DebtorDetailsDTO;
import pl.bykowski.rectangleapp.services.tdo_services.DebtorDetailsDTOService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//@RunWith(SpringRunner.class)
@RunWith(JUnitParamsRunner.class)
public class DebtorDetailsDTOServiceTest {

//    @InjectMocks
    private DebtorDetailsDTOService debtorDetailsDTOService;

    @Before
    public void init(){
        debtorDetailsDTOService = new DebtorDetailsDTOService();
    }

    @Test
    public void should_return_debtorDetailsDTOList_based_on_debtorDetailsList(){
        //given
        DebtorDetails debtorDetails1 = new DebtorDetails();
        debtorDetails1.setId(1L);
        debtorDetails1.setDebt(new BigDecimal(10));
        debtorDetails1.setReasonForTheDebt("coffee");

        DebtorDetails debtorDetails2 = new DebtorDetails();
        debtorDetails2.setId(2L);
        debtorDetails2.setDebt(new BigDecimal(11));
        debtorDetails2.setReasonForTheDebt("milk");
        List<DebtorDetails> debtorDetailsList = Arrays.asList(debtorDetails1, debtorDetails2);
        //when
        List<DebtorDetailsDTO> found = debtorDetailsDTOService.returnDebtorDetailsDTOList(debtorDetailsList);
        //then
        assertThat(found.size()).isEqualTo(debtorDetailsList.size());

        assertThat(found.get(0).getDebt()).isEqualTo(debtorDetailsList.get(0).getDebt());
        assertThat(found.get(0).getId()).isEqualTo(debtorDetailsList.get(0).getId());
        assertThat(found.get(0).getReasonForTheDebt()).isEqualTo(debtorDetailsList.get(0).getReasonForTheDebt());

        assertThat(found.get(1).getDebt()).isEqualTo(debtorDetailsList.get(1).getDebt());
        assertThat(found.get(1).getId()).isEqualTo(debtorDetailsList.get(1).getId());
        assertThat(found.get(1).getReasonForTheDebt()).isEqualTo(debtorDetailsList.get(1).getReasonForTheDebt());
    }

    @Test
    public void should_return_debtorDetailsDTO_based_on_debtorDetails(){
        //given
        DebtorDetails debtorDetails = new DebtorDetails();
        debtorDetails.setId(44L);
        debtorDetails.setDebt(new BigDecimal(15));
        //when
        DebtorDetailsDTO found = debtorDetailsDTOService.returnDebtorDetailsDTO(debtorDetails);
        //then
        assertThat(found.getDebt()).isEqualTo(debtorDetails.getDebt());
        assertThat(found.getId()).isEqualTo(debtorDetails.getId());
    }
}
