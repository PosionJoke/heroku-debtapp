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
import pl.bykowski.rectangleapp.services.DebtorService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.junit.matchers.JUnitMatchers.hasItem;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DebtorControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private DebtorService debtorService;


    @Before
    public void init(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser("spring")
    @Test
    public void should_be_status_server_ok_get_debtorList() throws Exception {
        //given
        String userName = "spring";
//        Long id = 1L;
        String debtorName = "Adam";
        Debtor debtor1 = new Debtor();
        debtor1.setName(debtorName);
        List<Debtor> debtorList = Arrays.asList(debtor1);
        given(debtorService.findByUserName(userName)).willReturn(debtorList);

        //when
        mvc.perform(get("/debtor-list"))
                .andExpect(model().size(1))
                .andExpect(model().attribute("debtors", debtorList))
                .andExpect(model().attribute("debtors", hasSize(1)))
                .andExpect(model().attribute("debtors", equalTo(debtorList)))
                .andExpect(model().attribute("debtors", hasItem(
                        allOf(
                                hasProperty("name", is(debtorName))
                        )
                )))
                .andExpect(view().name("debtor-list"))
                .andExpect(status().isOk());
        //then
        verify(debtorService).findByUserName(userName);
    }

    @WithMockUser("spring")
    @Test
    public void should_be_status_server_ok_get_debtorDebtEdit() throws Exception {
        //given
        //when
        //then
    }

}
