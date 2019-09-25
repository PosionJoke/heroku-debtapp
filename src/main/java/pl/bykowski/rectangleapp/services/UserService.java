package pl.bykowski.rectangleapp.services;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.bykowski.rectangleapp.model.DebtorUser;
import pl.bykowski.rectangleapp.repositories.DebtorUserRepo;

import java.util.Objects;

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class);

    private final DebtorUserRepo debtorUserRepo;

    public UserService(DebtorUserRepo debtorUserRepo) {
        this.debtorUserRepo = Objects.requireNonNull(debtorUserRepo, "debtorUserRepo must be not null");
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

    public boolean checkPassword(String password1, String password2){
        return password1.equals(password2);
    }

    public DebtorUser findByName(String name){
       return debtorUserRepo.findByName(name).get();
    }

    public void save(DebtorUser debtorUser){
        logger.debug("Save User\nid : " + debtorUser.getId() +
                "\nname : " + debtorUser.getName() +
                "\nemail : " + debtorUser.getEmail());
        debtorUserRepo.save(debtorUser);
    }
}
