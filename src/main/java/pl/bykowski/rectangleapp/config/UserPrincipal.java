package pl.bykowski.rectangleapp.config;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.bykowski.rectangleapp.model.DebtorUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {

    private final DebtorUser debtorUser;

    UserPrincipal(DebtorUser debtorUser) {
        this.debtorUser = debtorUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        this.debtorUser.getRoles().forEach(r -> {
            GrantedAuthority authority = new SimpleGrantedAuthority(r.getName());
            authorities.add(authority);
        });

        return authorities;
    }

    @Override
    public String getPassword() {
        return debtorUser.getPassword();
    }

    @Override
    public String getUsername() {
        return debtorUser.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.debtorUser.getActive() == 1;
    }
}
