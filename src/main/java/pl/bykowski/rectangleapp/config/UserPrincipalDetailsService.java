package pl.bykowski.rectangleapp.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import pl.bykowski.rectangleapp.repositories.DebtorUserRepo;
import pl.bykowski.rectangleapp.model.DebtorUser;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {

    private DebtorUserRepo debtorUserRepo;

    public UserPrincipalDetailsService(DebtorUserRepo debtorUserRepo) {
        this.debtorUserRepo = Objects.requireNonNull(debtorUserRepo, "debtorUserRepo must be not null");
    }

    @Override
    public UserDetails loadUserByUsername(String userName)  {
        Optional<DebtorUser> debtorUser = this.debtorUserRepo.findByName(userName);
//        return new UserPrincipal(debtorUser);

        if(debtorUser.isPresent()){
            return new UserPrincipal(debtorUser.get());
        }else{
            //todo logger slot here
            return new UserPrincipal(new DebtorUser());
        }
    }
}
