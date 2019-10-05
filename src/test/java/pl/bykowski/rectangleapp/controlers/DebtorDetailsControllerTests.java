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
import org.springframework.util.LinkedMultiValueMap;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        DebtorDetails debtorDetails3 = new DebtorDetails();
        debtorDetails2.setName("Adrian3");
        debtorDetails2.setDebt(new BigDecimal(3));
        debtorDetailsList = Arrays.asList(debtorDetails1, debtorDetails2, debtorDetails3);

        DebtorDetailsDTO debtorDetailsDTO1 = new DebtorDetailsDTO();
        debtorDetailsDTO1.setName("Adrian1");
        debtorDetailsDTO1.setDebt(new BigDecimal(1));
        DebtorDetailsDTO debtorDetailsDTO2 = new DebtorDetailsDTO();
        debtorDetailsDTO2.setName("Adrian2");
        debtorDetailsDTO2.setDebt(new BigDecimal(2));
        DebtorDetailsDTO debtorDetailsDTO3 = new DebtorDetailsDTO();
        debtorDetailsDTO2.setName("Adrian3");
        debtorDetailsDTO2.setDebt(new BigDecimal(3));
        debtorDetailsDTOList = Arrays.asList(debtorDetailsDTO1, debtorDetailsDTO2, debtorDetailsDTO3);
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
    public void should_return_debtorDetailsDTO() throws Exception{
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
                .andExpect(model().size(2))
                .andExpect(status().isOk());
        //then
        verify(debtorDetailsDTOService).returnDebtorDetailsDTO(debtorDetails);
    }

    @WithMockUser(TEST_USER_NAME)
    @Test
    public void should_return_new_debtorDetailsDTO() throws Exception{
        //given
        String name = "Ada";
        //when
        mvc.perform(get("/make-new-debtor-details")
                .param("name", name)
        )
                .andExpect(view().name("make-new-debtor-details"))
                .andExpect(model().attribute("debtorDetails", new DebtorDetailsDTO()))
                .andExpect(model().size(2))
                .andExpect(status().isOk());
        //then
    }

    @WithMockUser(TEST_USER_NAME)
    @Test
    public void should_return_debtorDetailsDTOList_and_deleteAndUpdateDebt() throws Exception{
        //given
        Long id = 1L;
        given(principal.getName()).willReturn(TEST_USER_NAME);
        given(debtorDetailsService.findByUserName(principal.getName())).willReturn(debtorDetailsList);
        given(debtorDetailsDTOService.returnDebtorDetailsDTOList(debtorDetailsList)).willReturn(debtorDetailsDTOList);
        //when
        mvc.perform(get("/debtor-details-delete-by-id")
                .param("id", String.valueOf(id))
                .flashAttr("principal", principal)
        )
                .andExpect(view().name("debtor-details-list"))
                .andExpect(model().attribute("debtorLIST", debtorDetailsDTOList))
                .andExpect(model().size(2))
                .andExpect(status().isOk());
        //then
        verify(debtorService).deleteDebtorDetailsUpdateTotalDebtMakeNewDebtorHistory(id, principal.getName());
    }

    @WithMockUser(TEST_USER_NAME)
    @Test
    public void example() throws Exception{
        //given
        String name = "Adrian";
        DebtorDetailsDTO debtorDetails = new DebtorDetailsDTO();
        debtorDetails.setName(name);

        Debtor debtor = new Debtor();
        debtor.setName(name);
        given(debtorService.findDebtorByName(name)).willReturn(debtor);
        given(principal.getName()).willReturn(TEST_USER_NAME);
        //when
        mvc.perform(post("/make-new-debtor-details")
                .flashAttr("debtorDetails", debtorDetails)
                .flashAttr("principal", principal)
                .param("name", name)
        )
                .andExpect(model().size(4))
                .andExpect(status().isOk());
        //then
        verify(debtorService).updateTotalDebtAndMakeNewDebtorDetails(debtorDetails, debtor, principal.getName());
    }
}
