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
import pl.bykowski.rectangleapp.model.DebtorUser;
import pl.bykowski.rectangleapp.model.dto.DebtorUserDTO;
import pl.bykowski.rectangleapp.model.dto.UserDTO;
import pl.bykowski.rectangleapp.services.UserService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
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
    @MockBean
    private UserService userService;

    private DebtorUserDTO debtorUserDTO = new DebtorUserDTO();

    @Before
    public void init() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        debtorUserDTO.setEmail("email@email.com");
        debtorUserDTO.setName("Ada");
    }

    @WithMockUser(TEST_USER_NAME)
    @Test
    public void should_return_view_name_createNewUser_when_passwords_are_not_equals() throws Exception {
        debtorUserDTO.setPassword1("1234");
        debtorUserDTO.setPassword2("1235");
        given(bindingResult.hasErrors()).willReturn(true);

        mvc.perform(post("/create-new-user")
                .flashAttr("debtorUserDTO", debtorUserDTO)
                .flashAttr("bindingResult", bindingResult)
        )
                .andExpect(view().name("create-new-user"))
                .andExpect(status().isOk());
    }

    @WithMockUser(TEST_USER_NAME)
    @Test
    public void should_return_view_name_createNewUserAuthentication_when_passwords_are_equals() throws Exception {
        debtorUserDTO.setPassword1("1234");
        debtorUserDTO.setPassword2("1234");
        given(bindingResult.hasErrors()).willReturn(true);
        UserDTO userDTO = new UserDTO();
        given(userService.makeNewUser(debtorUserDTO)).willReturn(userDTO);

        mvc.perform(post("/create-new-user")
                .flashAttr("debtorUserDTO", debtorUserDTO)
                .flashAttr("bindingResult", bindingResult)
        )
                .andExpect(view().name("create-new-user-authentication"))
                .andExpect(status().isOk());
    }

    @WithMockUser(TEST_USER_NAME)
    @Test
    public void should_return_model_name_defaultView_when_authentication_code_is_correct() throws Exception {
        DebtorUserDTO debtorUserDTO = new DebtorUserDTO();
        debtorUserDTO.setAuthenticationCode("1231212");
        debtorUserDTO.setAuthenticationCodeInput("1231212");

        given(userService.checkAuthenticationCode(
                debtorUserDTO.getAuthenticationCode(), debtorUserDTO.getAuthenticationCodeInput()))
                .willReturn(true);

        DebtorUser debtorUser = new DebtorUser();
        debtorUser.setName("Ada");
        given(userService.findByName(debtorUserDTO.getName())).willReturn(java.util.Optional.of(debtorUser));

        mvc.perform(post("/create-new-user-authentication")
                .flashAttr("debtorUserDTO", debtorUserDTO)
        )
                .andExpect(view().name("create-new-user-authentication"))
                .andExpect(status().isOk());
        //then
        verify(userService).save(debtorUser);
    }

    @WithMockUser(TEST_USER_NAME)
    @Test
    public void should_return_model_name_createNewUserAuthentication_when_authentication_code_is_incorrect() throws Exception {
        DebtorUserDTO debtorUserDTO = new DebtorUserDTO();
        debtorUserDTO.setAuthenticationCode("1231212");
        debtorUserDTO.setAuthenticationCodeInput("1231212");

        given(userService.checkAuthenticationCode(
                debtorUserDTO.getAuthenticationCode(), debtorUserDTO.getAuthenticationCodeInput()))
                .willReturn(false);

        DebtorUser debtorUser = new DebtorUser();
        given(userService.findByName(debtorUserDTO.getName())).willReturn(java.util.Optional.of(debtorUser));

        mvc.perform(post("/create-new-user-authentication")
                .flashAttr("debtorUserDTO", debtorUserDTO)
        )
                .andExpect(view().name("create-new-user-authentication"))
                .andExpect(status().isOk());
    }
}
