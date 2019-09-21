package pl.bykowski.rectangleapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //todo lets check is this field below should be static?
    private UserPrincipalDetailsService userPrincipalDetailsService;
    //soon in use
    private static final String ADMIN = "ADMIN";
    private static final String MANAGER = "MANAGER";

    public SecurityConfig(UserPrincipalDetailsService userPrincipalDetailsService) {
        this.userPrincipalDetailsService = Objects.requireNonNull(userPrincipalDetailsService, "userPrincipalDetailsService must be not null");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().disable()
                    .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/create-new-user", "/figureDB", "/default-view", "/", "/create-new-user-authentication").permitAll()
                .antMatchers("/testUser").hasAnyRole("USER")
                .anyRequest().authenticated()
                    .and()
                .formLogin().permitAll()
                    .and()
                .logout().permitAll()
                    .logoutSuccessUrl("/default-view");
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userPrincipalDetailsService);

        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
