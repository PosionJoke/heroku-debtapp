package pl.bykowski.rectangleapp.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.bykowski.rectangleapp.model.DebtorUser;
import pl.bykowski.rectangleapp.repositories.DebtorUserRepo;

import java.util.Objects;

@Service
public class UserService {

    private final DebtorUserRepo debtorUserRepo;

    public UserService(DebtorUserRepo debtorUserRepo) {
        this.debtorUserRepo = Objects.requireNonNull(debtorUserRepo, "debtorUserRepo must be not null");
    }

    public String findUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public String generateNewAuthenticationCode(){
        return String.valueOf((int)(Math.random() * 1000000));
    }

    public boolean checkAuthenticationCode(String authCode, String authCodeInput){
        return authCode.equals(authCodeInput);
    }

    public boolean checkPassword(String password1, String password2){
        return password1.equals(password2);
    }

    public DebtorUser findByName(String name){
       return debtorUserRepo.findByName(name);
    }

    public void save(DebtorUser debtorUser){
        debtorUserRepo.save(debtorUser);
    }
}
