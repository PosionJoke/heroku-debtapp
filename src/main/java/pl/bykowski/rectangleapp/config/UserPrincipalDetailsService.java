package pl.bykowski.rectangleapp.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import pl.bykowski.rectangleapp.repositories.DebtorUserRepo;
import pl.bykowski.rectangleapp.model.DebtorUser;

import java.util.Objects;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {

    private DebtorUserRepo debtorUserRepo;

    public UserPrincipalDetailsService(DebtorUserRepo debtorUserRepo) {
        this.debtorUserRepo = Objects.requireNonNull(debtorUserRepo, "debtorUserRepo must be not null");
    }

    @Override
    public UserDetails loadUserByUsername(String s)  {
        DebtorUser debtorUser = this.debtorUserRepo.findByName(s);
        return new UserPrincipal(debtorUser);
    }
}
