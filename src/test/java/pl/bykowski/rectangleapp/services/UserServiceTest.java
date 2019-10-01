package pl.bykowski.rectangleapp.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import pl.bykowski.rectangleapp.model.DebtorUser;
import pl.bykowski.rectangleapp.model.Role;
import pl.bykowski.rectangleapp.model.dto.DebtorUserDTO;
import pl.bykowski.rectangleapp.model.dto.UserDTO;
import pl.bykowski.rectangleapp.repositories.DebtorUserRepo;
import pl.bykowski.rectangleapp.repositories.RoleRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@RunWith(SpringRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private DebtorUserRepo debtorUserRepo;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private NotificationService notificationService;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContextHolder securityContextHolder;

    private DebtorUserDTO debtorUserDTO = new DebtorUserDTO();
    private String debtorName;
    private String roleName;
    private Long roleId;
    private String password2;
    private String email;
    private Role role = new Role();


    @Before
    public void init() {
        debtorName = "Ada";
        roleName = "ROLE_USER";
        password2 = "admin1234";
        email = "example@email.com";
        roleId = 2L;
        debtorUserDTO.setName(debtorName);
        debtorUserDTO.setEmail(email);
        debtorUserDTO.setPassword2(password2);

        role.setName(roleName);
        role.setId(roleId);
    }


    @Test
    public void should_return_UserDTO_based_on_DebtorUserDTO() {
        //given
        given(roleRepository.findByName(roleName)).willReturn(java.util.Optional.ofNullable(role));
        given(passwordEncoder.encode(debtorUserDTO.getPassword2())).willReturn("1234");
        //when
        UserDTO created = userService.makeNewUser(debtorUserDTO);
        //then
        assertThat(created.getName()).isEqualTo(debtorUserDTO.getName());
    }

    @Test
    public void should_return_true_when_codes_are_the_same() {
        //given
        String authCode1 = "2";
        String authCode2 = "2";
        //when
        boolean shouldBeTrue = userService.checkAuthenticationCode(authCode1, authCode2);
        //then
        assertThat(shouldBeTrue).isEqualTo(true);
    }

    @Test
    public void should_return_false_when_codes_are_not_the_same() {
        //given
        String authCode1 = "1";
        String authCode2 = "2";
        //when
        boolean shouldBeTrue = userService.checkAuthenticationCode(authCode1, authCode2);
        //then
        assertThat(shouldBeTrue).isEqualTo(false);
    }

    @Test
    public void should_save_debtorUser() {
        //given
        DebtorUser debtorUser = new DebtorUser();
        //when
        userService.save(debtorUser);
        //then
        verify(debtorUserRepo).save(debtorUser);
    }

}
