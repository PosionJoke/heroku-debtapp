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
import pl.bykowski.rectangleapp.model.dto.DebtorDTO;
import pl.bykowski.rectangleapp.model.dto.DebtorDetailsDTO;
import pl.bykowski.rectangleapp.services.CurrencyService;
import pl.bykowski.rectangleapp.services.DebtorService;
import pl.bykowski.rectangleapp.services.tdo_services.DebtorDTOService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DebtorControllerTest {

    private static final String TEST_USER_NAME = "testUser";
    private static final String currencyRate = "1";
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private DebtorService debtorService;
    @MockBean
    private DebtorDTOService debtorDTOService;
    @MockBean
    private Principal principal;
    @MockBean
    private CurrencyService currencyService;

    private List<Debtor> debtorList;
    private List<DebtorDTO> debtorDTOList;

    @Before
    public void init() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        Debtor debtor = Debtor.builder()
                .id(1L)
                .totalDebt(new BigDecimal(1))
                .name("Adrian")
                .build();

        Debtor debtor1 = Debtor.builder()
                .id(2L)
                .totalDebt(new BigDecimal(2))
                .name("Adrian1")
                .build();

        Debtor debtor2 = Debtor.builder()
                .id(3L)
                .totalDebt(new BigDecimal(3))
                .name("Adrian2")
                .build();

        debtorList = Arrays.asList(debtor, debtor1, debtor2);

        DebtorDTO debtorDTO = new DebtorDTO();
        debtorDTO.setId(1L);
        debtorDTO.setTotalDebt(new BigDecimal(1));
        debtorDTO.setName("Adrian");
        DebtorDTO debtorDTO1 = new DebtorDTO();
        debtorDTO1.setId(2L);
        debtorDTO1.setTotalDebt(new BigDecimal(2));
        debtorDTO1.setName("Adrian1");
        DebtorDTO debtorDTO2 = new DebtorDTO();
        debtorDTO2.setId(3L);
        debtorDTO2.setTotalDebt(new BigDecimal(3));
        debtorDTO2.setName("Adrian2");

        debtorDTOList = Arrays.asList(debtorDTO, debtorDTO1, debtorDTO2);
    }

    @WithMockUser(TEST_USER_NAME)
    @Test
    public void should_be_status_server_ok_get_debtorList() throws Exception {
        String userName = TEST_USER_NAME;
        String debtorName1 = "Adam";
        String debtorName2 = "Artur";

        Debtor debtor1 = Debtor.builder()
                .name(debtorName1)
                .userName(userName)
                .build();

        Debtor debtor2 = Debtor.builder()
                .name(debtorName2)
                .userName(userName)
                .build();

        List<Debtor> debtorList = Arrays.asList(debtor1, debtor2);
        DebtorDTO debtorDTO1 = new DebtorDTO();
        debtorDTO1.setName(debtorName1);
        DebtorDTO debtorDTO2 = new DebtorDTO();
        debtorDTO2.setName(debtorName2);
        List<DebtorDTO> debtorDTOList = Arrays.asList(debtorDTO1, debtorDTO2);

        given(debtorService.findByUserName(userName)).willReturn(debtorList);
        given(debtorDTOService.returnDebtorDTOList(debtorList)).willReturn(debtorDTOList);
        given(currencyService.calculateCurrencyRates("PLN", "PLN"))
                .willReturn(currencyRate);
        given(currencyService.setCurrencyRates(debtorDTOList, currencyRate))
                .willReturn(debtorDTOList);

        mvc.perform(get("/debtor-list"))
                .andExpect(model().size(3))
                .andExpect(model().attribute("debtors", debtorDTOList))
                .andExpect(model().attribute("debtors", hasSize(2)))
                .andExpect(view().name("debtor-list"))
                .andExpect(status().isOk())
                .andDo(print());
        //then
        verify(debtorService).findByUserName(userName);
    }

    @WithMockUser(TEST_USER_NAME)
    @Test
    public void should_be_status_server_ok_get_debtorDebtEdit() throws Exception {
        String debtorName = "Adam";
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("id", "1");
        requestParams.add("name", debtorName);

        Debtor debtor = Debtor.builder()
                .name(debtorName)
                .build();

        given(debtorService.findDebtorByName(debtorName)).willReturn(java.util.Optional.of(debtor));

        mvc.perform(get("/debtor-debt-edit")
                .params(requestParams)
        )
                .andExpect(status().isOk())
                .andExpect(model().size(3))
                .andExpect(view().name("debtor-debt-edit"))
                .andExpect(model().attribute("id", 1L))
                .andExpect(model().attribute("debtor", new DebtorDTO()))
                .andExpect(model().attribute("name", "Adam"));
        //then
        verify(debtorDTOService).returnDebtorDTO(debtor);
    }

    @WithMockUser(TEST_USER_NAME)
    @Test
    public void should_update_total_debt_and_return_debtorDTOList() throws Exception {
        Long id = 1L;
        BigDecimal totalDebt = new BigDecimal(100);

        Debtor debtorReturn = Debtor.builder()
                .id(id)
                .totalDebt(totalDebt)
                .build();

        given(debtorService.findById(id)).willReturn(java.util.Optional.of(debtorReturn));
        given(principal.getName()).willReturn(TEST_USER_NAME);
        given(debtorService.findByUserName(TEST_USER_NAME)).willReturn(debtorList);
        given(debtorDTOService.returnDebtorDTOList(debtorList)).willReturn(debtorDTOList);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("id", "1");

        mvc.perform(post("/debtor-save")
                .params(requestParams)
                .flashAttr("debtorDTO", new DebtorDTO())
                .flashAttr("principal", principal)
        )
                .andExpect(status().isFound());
        //then
        verify(debtorService).updateTotalDebt(id, totalDebt);
    }

    @WithMockUser(TEST_USER_NAME)
    @Test
    public void should_add_new_debtor_and_return_debtorDTOList() throws Exception {
        DebtorDetailsDTO debtorDetailsDTO = new DebtorDetailsDTO();
        String debtorDTOName = "Ada";
        BigDecimal debtValue = new BigDecimal(10);
        String reasonForTheDebt = "Coffee";
        String dateString = "2050-10-10";
        debtorDetailsDTO.setName(debtorDTOName);
        debtorDetailsDTO.setDebt(debtValue);
        debtorDetailsDTO.setReasonForTheDebt(reasonForTheDebt);
        debtorDetailsDTO.setDebtEndDateString(dateString);

        given(principal.getName()).willReturn(TEST_USER_NAME);
        given(debtorService.findByUserName(principal.getName())).willReturn(debtorList);
        given(debtorDTOService.returnDebtorDTOList(debtorList)).willReturn(debtorDTOList);

        mvc.perform(post("/debtor-create")
                .flashAttr("debtorDetailsDTO", debtorDetailsDTO)
                .flashAttr("principal", principal)
        )
                .andExpect(status().isOk());

        //then
        verify(debtorService).addNewDebtor(debtorDetailsDTO, principal.getName());
    }
}
