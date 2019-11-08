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
import pl.bykowski.rectangleapp.model.dto.DebtorDTO;
import pl.bykowski.rectangleapp.services.DebtorDetailsService;
import pl.bykowski.rectangleapp.services.DebtorService;
import pl.bykowski.rectangleapp.services.tdo_services.DebtorDTOService;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainViewControllerTest {

    private MockMvc mvc;
    private static final String TEST_USER_NAME = "testUser";

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private DebtorService debtorService;
    @MockBean
    private DebtorDetailsService debtorDetailsService;
    @MockBean
    private DebtorDTOService debtorDTOService;
    @MockBean
    private Principal principal;

    @Before
    public void init() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        given(principal.getName()).willReturn(TEST_USER_NAME);
    }

    @WithMockUser(TEST_USER_NAME)
    @Test
    public void should_return_model_name_mainView_when_debtsList_is_not_empty() throws Exception {

        DebtorDetails debtorDetails = DebtorDetails.builder().build();
        List<DebtorDetails> debtsList = Collections.singletonList(debtorDetails);
        given(debtorDetailsService.findByUserName(principal.getName())).willReturn(debtsList);

        Debtor debtor = new Debtor();
        given(debtorService.returnDebtorWithBiggestDebt(principal.getName())).willReturn(java.util.Optional.of(debtor));
        DebtorDTO debtorWithBiggestDebtDTO = new DebtorDTO();
        debtorWithBiggestDebtDTO.setName("Ada");
        given(debtorDTOService.returnDebtorDTO(debtor)).willReturn(debtorWithBiggestDebtDTO);
        DebtorDTO debtorWithBiggestCountOfDebts = new DebtorDTO();
        debtorWithBiggestCountOfDebts.setName("ADADA");
        given(debtorDTOService.returnDebtorDTOWithHighestCountOfDebts(principal.getName())).willReturn(debtorWithBiggestCountOfDebts);

        mvc.perform(post("/main-view")
                .flashAttr("principal", principal)
        )
                .andExpect(view().name("main-view"))
                .andExpect(model().attribute("debtorWithBiggestDebt", debtorWithBiggestDebtDTO))
                .andExpect(model().attribute("debtorWithHighestCountOfDebts", debtorWithBiggestCountOfDebts))
                .andExpect(status().isOk());
    }

    @WithMockUser(TEST_USER_NAME)
    @Test
    public void should_return_model_name_mainViewNewUser_when_debtsList_is_empty() throws Exception {

        mvc.perform(post("/main-view")
                .flashAttr("principal", principal)
        )
                .andExpect(view().name("main-view-new-user"))
                .andExpect(status().isOk());
    }
}
