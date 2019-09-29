package pl.bykowski.rectangleapp.services;

import io.vavr.concurrent.Future;
import org.apache.log4j.Logger;
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

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class);

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
        // make authentication code
        String authenticationCode = generateNewAuthenticationCode();

        //find and save role for new user
        Optional<Role> singleRole = roleRepository.findByName("ROLE_USER");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(singleRole.get());
        //create new user
        DebtorUser newDebtorUser = new DebtorUser(
                debtorUserDTO.getName(),
                passwordEncoder.encode(debtorUserDTO.getPassword2()),
                roleSet,
                "",
                debtorUserDTO.getEmail(),
                0,
                authenticationCode);

        //send mail
        Future.of(() -> notificationService.sendNotification(newDebtorUser.getEmail(), authenticationCode));

        //save new user
        debtorUserRepo.save(newDebtorUser);

        //make DTO user
        UserDTO userDTO = new UserDTO();
        userDTO.setAuthenticationCode(authenticationCode);
        userDTO.setName(newDebtorUser.getName());

        return userDTO;
    }

    public String findUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public String generateNewAuthenticationCode(){
        String authenticationCode = String.valueOf((int) (Math.random() * 1000000));
        logger.debug("Authentication code : " + authenticationCode);
        return authenticationCode;
    }

    public boolean checkAuthenticationCode(String authCode, String authCodeInput){
        return authCode.equals(authCodeInput);
    }

    public Optional<DebtorUser> findByName(String name) {
        return debtorUserRepo.findByName(name);
    }

    public void save(DebtorUser debtorUser){
        logger.debug("Save User\nid : " + debtorUser.getId() +
                "\nname : " + debtorUser.getName() +
                "\nemail : " + debtorUser.getEmail());
        debtorUserRepo.save(debtorUser);
    }
}
