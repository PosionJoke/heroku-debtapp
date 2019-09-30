package pl.bykowski.rectangleapp.controlers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.bykowski.rectangleapp.config.UserPrincipalDetailsService;
import pl.bykowski.rectangleapp.controller.GreetingController;
import pl.bykowski.rectangleapp.controller.todelete.EmployeeRestController;
import pl.bykowski.rectangleapp.repositories.DebtorDetailsRepo;
import pl.bykowski.rectangleapp.repositories.DebtorRepo;
import pl.bykowski.rectangleapp.services.*;
import pl.bykowski.rectangleapp.services.tdo_services.DebtorDTOService;
import pl.bykowski.rectangleapp.services.tdo_services.DebtorDetailsDTOService;
import pl.bykowski.rectangleapp.services.tdo_services.DebtorHistoryDTOService;

import static org.junit.matchers.JUnitMatchers.containsString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GreetingController.class)
public class GreetingTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GreetingService greetingService;

    @MockBean
    private UserPrincipalDetailsService userPrincipalDetailsService;


    @Test
    public void should_return_greeting() throws Exception {

        given(greetingService.greet()).willReturn("Hello World");

        mvc.perform(get("/greeting")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello World")));
    }

}
