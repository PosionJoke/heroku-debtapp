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
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;
import pl.bykowski.rectangleapp.model.dto.DebtorUserDTO;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    private static final String TEST_USER_NAME = "testUser";
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private BindingResult bindingResult;

    private DebtorUserDTO debtorUserDTO = new DebtorUserDTO();

    @Before
    public void init() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

//        @NotEmpty
//        @Size(min = 2, message = "Name should have at least 2 characters")
//        private String name;
//        @Email
//        private String email;
//        @NotEmpty
//        private String password1;
//        @NotEmpty
//        private String password2;

        debtorUserDTO.setEmail("email@email.com");
        debtorUserDTO.setName("Ada");
    }

    @WithMockUser(TEST_USER_NAME)
    @Test
    public void should_return_view_name_createNewUser_when_passwords_are_not_equals() throws Exception{
        //given
        debtorUserDTO.setPassword1("1234");
        debtorUserDTO.setPassword2("1235");
        given(bindingResult.hasErrors()).willReturn(true);
        //when
        mvc.perform(post("/create-new-user")
                .flashAttr("debtorUserDTO", debtorUserDTO)
                .flashAttr("bindingResult", bindingResult)
        )
                .andExpect(view().name("create-new-user"))
                .andExpect(status().isOk());
        //then
    }

    @WithMockUser(TEST_USER_NAME)
    @Test
    public void should_return_view_name_createNewUserAuthentication_when_passwords_are_equals() throws Exception{
        //given
        debtorUserDTO.setPassword1("1234");
        debtorUserDTO.setPassword2("1234");
        given(bindingResult.hasErrors()).willReturn(true);
        //when
        mvc.perform(post("/create-new-user")
                .flashAttr("debtorUserDTO", debtorUserDTO)
                .flashAttr("bindingResult", bindingResult)
        )
                .andExpect(view().name("create-new-user-authentication"))
                .andExpect(status().isOk());
        //then
    }

}
