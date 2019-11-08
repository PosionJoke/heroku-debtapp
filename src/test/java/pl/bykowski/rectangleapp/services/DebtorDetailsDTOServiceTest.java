package pl.bykowski.rectangleapp.services;

import junitparams.JUnitParamsRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.model.dto.DebtorDetailsDTO;
import pl.bykowski.rectangleapp.services.tdo_services.DebtorDetailsDTOService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class DebtorDetailsDTOServiceTest {

    private DebtorDetailsDTOService debtorDetailsDTOService;

    @Before
    public void init() {
        debtorDetailsDTOService = new DebtorDetailsDTOService();
    }

    @Test
    public void should_return_debtorDetailsDTOList_based_on_debtorDetailsList() {
        //given
        DebtorDetails debtorDetails1 = DebtorDetails.builder()
                .id(1L)
                .debt(new BigDecimal(10))
                .reasonForTheDebt("coffee")
                .build();

        DebtorDetails debtorDetails2 = DebtorDetails.builder()
                .id(2L)
                .debt(new BigDecimal(11))
                .reasonForTheDebt("milk")
                .build();
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
    public void should_return_debtorDetailsDTO_based_on_debtorDetails() {
        //given
        DebtorDetails debtorDetails = DebtorDetails.builder()
                .id(44L)
                .debt(new BigDecimal(15))
                .build();
        //when
        DebtorDetailsDTO found = debtorDetailsDTOService.returnDebtorDetailsDTO(debtorDetails);
        //then
        assertThat(found.getDebt()).isEqualTo(debtorDetails.getDebt());
        assertThat(found.getId()).isEqualTo(debtorDetails.getId());
    }
}
