package pl.bykowski.rectangleapp.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.bykowski.rectangleapp.repositories.repo_interfaces.DebtorUserRepo;
import pl.bykowski.rectangleapp.repositories.repo_struct.DebtorUser;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {

    private DebtorUserRepo debtorUserRepo;

    public UserPrincipalDetailsService(DebtorUserRepo debtorUserRepo) {
        this.debtorUserRepo = debtorUserRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        DebtorUser debtorUser = this.debtorUserRepo.findByName(s).get(0);
        UserPrincipal userPrincipal = new UserPrincipal(debtorUser);

        return userPrincipal;
    }
}
