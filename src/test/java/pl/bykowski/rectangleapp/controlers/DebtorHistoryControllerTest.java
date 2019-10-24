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
import pl.bykowski.rectangleapp.model.DebtorHistory;
import pl.bykowski.rectangleapp.model.dto.DebtorHistoryDTO;
import pl.bykowski.rectangleapp.services.CurrencyService;
import pl.bykowski.rectangleapp.services.DebtorHistoryService;
import pl.bykowski.rectangleapp.services.tdo_services.DebtorHistoryDTOService;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DebtorHistoryControllerTest {

    private static final String TEST_USER_NAME = "testUser";
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private Principal principal;
    @MockBean
    private DebtorHistoryService debtorHistoryService;
    @MockBean
    private DebtorHistoryDTOService debtorHistoryDTOService;
    @MockBean
    private CurrencyService currencyService;

    @Before
    public void init() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(TEST_USER_NAME)
    @Test
    public void should_go_to_view_named_debtorHistoryList() throws Exception {
        DebtorHistory debtorHistory = new DebtorHistory();
        List<DebtorHistory> debtorHistoryList = Arrays.asList(debtorHistory);
        DebtorHistoryDTO debtorHistoryDTO = new DebtorHistoryDTO();
        List<DebtorHistoryDTO> debtorHistoryDTOS = Arrays.asList(debtorHistoryDTO);
        given(principal.getName()).willReturn(TEST_USER_NAME);
        given(debtorHistoryService.findByUserName(principal.getName())).willReturn(debtorHistoryList);
        given(debtorHistoryDTOService.returnDebtorHistoryDTOList(debtorHistoryList)).willReturn(debtorHistoryDTOS);
        given(currencyService.calculateCurrencyRates("PLN", "PLN"))
                .willReturn("1");

        mvc.perform(get("/debtor-history-list")
                .flashAttr("principal", principal)
        )
                .andExpect(view().name("debtor-history-list"))
                .andExpect(status().isOk());
    }
}
