package pl.bykowski.rectangleapp.config;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.bykowski.rectangleapp.model.DebtorUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class UserPrincipal implements UserDetails {

    private DebtorUser debtorUser;

     UserPrincipal(DebtorUser debtorUser) {
        this.debtorUser = Objects.requireNonNull(debtorUser, "debtorUser must be not null");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
// todo HELLO ADRIAN THERE IS SOME MESS, CLEAN UP THIS AND TAKE CARE OF LOGIC BELOW

//        this.debtorUser.getPermissions().forEach(p -> {
//            GrantedAuthority authority = new SimpleGrantedAuthority(p);
//            authorities.add(authority);
//        });

        this.debtorUser.getRoles().forEach(r -> {
            GrantedAuthority authority = new SimpleGrantedAuthority(r.getName());
            authorities.add(authority);
        });
//        authorities = this.debtorUser.getRoles().stream()
//                .map(role -> new SimpleGrantedAuthority(role.getName()))
//                .collect(Collectors.toList());
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
