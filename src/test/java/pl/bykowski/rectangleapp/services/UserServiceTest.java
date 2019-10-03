package pl.bykowski.rectangleapp.services;

import junitparams.JUnitParamsRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.bykowski.rectangleapp.model.DebtorUser;
import pl.bykowski.rectangleapp.model.Role;
import pl.bykowski.rectangleapp.model.dto.DebtorUserDTO;
import pl.bykowski.rectangleapp.model.dto.UserDTO;
import pl.bykowski.rectangleapp.repositories.DebtorUserRepo;
import pl.bykowski.rectangleapp.repositories.RoleRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


@RunWith(JUnitParamsRunner.class)
public class UserServiceTest {

    private UserService userService;
    private DebtorUserRepo debtorUserRepo;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    private DebtorUserDTO debtorUserDTO = new DebtorUserDTO();
    private String debtorName;
    private String roleName;
    private Long roleId;
    private String password2;
    private String email;
    private Role role = new Role();


    @Before
    public void init() {
        debtorUserRepo = mock(DebtorUserRepo.class);
        roleRepository = mock(RoleRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        NotificationService notificationService = mock(NotificationService.class);

        userService = new UserService(debtorUserRepo, roleRepository, passwordEncoder, notificationService);


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
