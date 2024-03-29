package pl.bykowski.rectangleapp.services;

import io.vavr.concurrent.Future;
import lombok.extern.log4j.Log4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.bykowski.rectangleapp.model.DebtorUser;
import pl.bykowski.rectangleapp.model.Role;
import pl.bykowski.rectangleapp.model.dto.DebtorUserDTO;
import pl.bykowski.rectangleapp.model.dto.UserDTO;
import pl.bykowski.rectangleapp.repositories.DebtorUserRepo;
import pl.bykowski.rectangleapp.repositories.RoleRepository;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Log4j
@Service
public class UserService {

    private final DebtorUserRepo debtorUserRepo;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;

    public UserService(DebtorUserRepo debtorUserRepo, RoleRepository roleRepository, PasswordEncoder passwordEncoder, NotificationService notificationService) {
        this.debtorUserRepo = Objects.requireNonNull(debtorUserRepo, "debtorUserRepo must be not null");
        this.roleRepository = Objects.requireNonNull(roleRepository, "roleRepository must be not null");
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder, "passwordEncoder must be not null");
        this.notificationService = Objects.requireNonNull(notificationService, "notificationService must be not null");
    }

    public UserDTO makeNewUser(DebtorUserDTO debtorUserDTO) {
        String authenticationCode = generateNewAuthenticationCode();

        Optional<Role> singleRole = roleRepository.findByName("ROLE_USER");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(singleRole.get());

        DebtorUser newDebtorUser = new DebtorUser(
                debtorUserDTO.getName(),
                passwordEncoder.encode(debtorUserDTO.getPassword2()),
                roleSet,
                "",
                debtorUserDTO.getEmail(),
                0,
                authenticationCode);


        Future.of(() -> notificationService.sendNotification(newDebtorUser.getEmail(), authenticationCode));

        debtorUserRepo.save(newDebtorUser);

        UserDTO userDTO = new UserDTO();
        userDTO.setAuthenticationCode(authenticationCode);
        userDTO.setName(newDebtorUser.getName());

        return userDTO;
    }

    String findUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private String generateNewAuthenticationCode(){
        String authenticationCode = String.valueOf((int) (Math.random() * 1000000));
        log.debug("Authentication code : " + authenticationCode);
        return authenticationCode;
    }

    public boolean checkAuthenticationCode(String authCode, String authCodeInput){
        return authCode.equals(authCodeInput);
    }

    public Optional<DebtorUser> findByName(String name) {
        return debtorUserRepo.findByName(name);
    }

    public void save(DebtorUser debtorUser){

        log.debug(String.format("Save User id : [%s], name : [%s], email : [%s]",debtorUser.getId(),
                debtorUser.getName(), debtorUser.getEmail()));

        debtorUserRepo.save(debtorUser);
    }
}
