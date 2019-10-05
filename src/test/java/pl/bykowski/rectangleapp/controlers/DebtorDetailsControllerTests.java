package pl.bykowski.rectangleapp.controlers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.model.dto.DebtorDetailsDTO;
import pl.bykowski.rectangleapp.repositories.DebtorDetailsRepo;
import pl.bykowski.rectangleapp.services.DebtorDetailsService;
import pl.bykowski.rectangleapp.services.DebtorService;
import pl.bykowski.rectangleapp.services.tdo_services.DebtorDetailsDTOService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DebtorDetailsControllerTests {

    private static final String TEST_USER_NAME = "testUser";
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private DebtorDetailsRepo debtorDetailsRepo;
    @MockBean
    private DebtorDetailsService debtorDetailsService;
    @MockBean
    private DebtorService debtorService;
    @MockBean
    private DebtorDetailsDTOService debtorDetailsDTOService;
    @MockBean
    private Principal principal;

    private List<DebtorDetails> debtorDetailsList;
    private List<DebtorDetailsDTO> debtorDetailsDTOList;

    @Before
    public void init(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        DebtorDetails debtorDetails1 = new DebtorDetails();
        debtorDetails1.setName("Adrian1");
        debtorDetails1.setDebt(new BigDecimal(1));
        DebtorDetails debtorDetails2 = new DebtorDetails();
        debtorDetails2.setName("Adrian2");
        debtorDetails2.setDebt(new BigDecimal(2));
        debtorDetailsList = Arrays.asList(debtorDetails1, debtorDetails2);

        DebtorDetailsDTO debtorDetailsDTO1 = new DebtorDetailsDTO();
        debtorDetailsDTO1.setName("Adrian1");
        debtorDetailsDTO1.setDebt(new BigDecimal(1));
        DebtorDetailsDTO debtorDetailsDTO2 = new DebtorDetailsDTO();
        debtorDetailsDTO2.setName("Adrian2");
        debtorDetailsDTO2.setDebt(new BigDecimal(2));
        debtorDetailsDTOList = Arrays.asList(debtorDetailsDTO1, debtorDetailsDTO2);
    }

    @WithMockUser(TEST_USER_NAME)
    @Test
    public void should_return_debtorDetailsDTOList() throws Exception{
        //given
        given(principal.getName()).willReturn(TEST_USER_NAME);
        given(debtorDetailsService.findByUserName(principal.getName())).willReturn(debtorDetailsList);
        given(debtorDetailsDTOService.returnDebtorDetailsDTOList(debtorDetailsList)).willReturn(debtorDetailsDTOList);
        //when
        mvc.perform(get("/debtor-details-list")
                .flashAttr("principal", principal)
        )
                .andExpect(model().attribute("debtorLIST", debtorDetailsDTOList))
                .andExpect(model().size(2))
                .andExpect(view().name("debtor-details-list"))
                .andExpect(status().isOk());
        //then
        verify(debtorDetailsService).findByUserName(principal.getName());
        verify(debtorDetailsDTOService).returnDebtorDetailsDTOList(debtorDetailsList);
    }

    @WithMockUser(TEST_USER_NAME)
    @Test
    public void text() throws Exception{
        //given
        Long id = 1L;
        DebtorDetails debtorDetails = new DebtorDetails();
        debtorDetails.setId(id);

        DebtorDetailsDTO debtorDetailsDTO = new DebtorDetailsDTO();
        debtorDetailsDTO.setId(debtorDetails.getId());

        given(debtorDetailsDTOService.returnDebtorDetailsDTO(debtorDetails)).willReturn(debtorDetailsDTO);
        given(debtorDetailsRepo.findById(id)).willReturn(java.util.Optional.of(debtorDetails));
        //when
        mvc.perform(get("/debtor-details-debt-edit")
                .param("id", "1")
                .param("name", "Adrian")
        )
                .andExpect(status().isOk());
        //then
    }
}
