package pl.bykowski.rectangleapp.services;

import io.vavr.concurrent.Future;
import lombok.extern.log4j.Log4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.bykowski.rectangleapp.model.DebtorUser;
import pl.bykowski.rectangleapp.model.FriendListToken;
import pl.bykowski.rectangleapp.model.Role;
import pl.bykowski.rectangleapp.model.dto.DebtorUserDTO;
import pl.bykowski.rectangleapp.model.dto.UserDTO;
import pl.bykowski.rectangleapp.repositories.DebtorUserRepo;
import pl.bykowski.rectangleapp.repositories.FriendListTokenRepo;
import pl.bykowski.rectangleapp.repositories.RoleRepository;

import javax.persistence.EntityNotFoundException;
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
    //TODO THIS SHOULD BE SERVICE
    private final FriendListTokenRepo invitesToFriendListRepo;

    public UserService(DebtorUserRepo debtorUserRepo, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                       NotificationService notificationService, FriendListTokenRepo invitesToFriendListRepo) {
        this.debtorUserRepo = Objects.requireNonNull(debtorUserRepo, "debtorUserRepo must be not null");
        this.roleRepository = Objects.requireNonNull(roleRepository, "roleRepository must be not null");
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder, "passwordEncoder must be not null");
        this.notificationService = Objects.requireNonNull(notificationService, "notificationService must be not null");
        this.invitesToFriendListRepo = Objects.requireNonNull(invitesToFriendListRepo, "invitesToFriendListRepo must be not null");
    }

    //TODO IDK where but right now this program give permission to add two users with the same name witch is bad idea
    //TODO add validation in some annotation on controller side
    public UserDTO makeNewUser(DebtorUserDTO debtorUserDTO) {
        String authenticationCode = generateNewAuthenticationCode();

        Optional<Role> singleRole = roleRepository.findByName("ROLE_USER");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(singleRole.get());

        DebtorUser newDebtorUser = DebtorUser.builder()
                .name(debtorUserDTO.getName())
                .password(passwordEncoder.encode(debtorUserDTO.getPassword2()))
                .roles(roleSet)
                .email(debtorUserDTO.getEmail())
                .authenticationCode(authenticationCode)
                .build();

        debtorUserRepo.save(newDebtorUser);

        FriendListToken invitesToFriendList = FriendListToken.builder()
                .userName(debtorUserDTO.getName())
                .userId(newDebtorUser.getId())
                .build();

        invitesToFriendListRepo.save(invitesToFriendList);

        Future.of(() -> notificationService.sendNotification(newDebtorUser.getEmail(), authenticationCode));

        UserDTO userDTO = new UserDTO();
        userDTO.setAuthenticationCode(authenticationCode);
        userDTO.setName(newDebtorUser.getName());

        return userDTO;
    }

    String findUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private String generateNewAuthenticationCode() {
        String authenticationCode = String.valueOf((int) (Math.random() * 1000000));
        log.debug("Authentication code : " + authenticationCode);
        return authenticationCode;
    }

    public boolean checkAuthenticationCode(String authCode, String authCodeInput) {
        return authCode.equals(authCodeInput);
    }

    public DebtorUser findByName(String name) {
        Optional<DebtorUser> debtorUserOpt = debtorUserRepo.findByName(name);
        return debtorUserOpt.orElseThrow(() -> new EntityNotFoundException(
                String.format("Unable to get DebtorUser name : [%s]", name)));
    }

    public DebtorUser findById(Long id){
        Optional<DebtorUser> debtorUserOpt = debtorUserRepo.findById(id);
        return debtorUserOpt.orElseThrow(() -> new EntityNotFoundException(
                String.format("Unable to get DebtorUser id : [%s]", id)));
    }

    public void save(DebtorUser debtorUser) {

        log.debug(String.format("Save User id : [%s], name : [%s], email : [%s]", debtorUser.getId(),
                debtorUser.getName(), debtorUser.getEmail()));

        debtorUserRepo.save(debtorUser);
    }
}
