package pl.bykowski.rectangleapp.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.bykowski.rectangleapp.repositories.repo_interfaces.DebtorUserRepo;
import pl.bykowski.rectangleapp.repositories.repo_struct.DebtorUser;

import java.util.Arrays;
import java.util.List;

@Service
public class DbInit implements CommandLineRunner {

    private DebtorUserRepo debtorUserRepo;
    private PasswordEncoder passwordEncoder;

    public DbInit(DebtorUserRepo debtorUserRepo, PasswordEncoder passwordEncoder) {
        this.debtorUserRepo = debtorUserRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        this.debtorUserRepo.deleteAll();

        DebtorUser admin = new DebtorUser("admin", passwordEncoder.encode("1234"), "ADMIN", "ACCESS_TEST1,ACCESS_TEST2");
        DebtorUser manager = new DebtorUser("manager", passwordEncoder.encode("1234"), "MANAGER", "ACCESS_TEST2");
        DebtorUser user = new DebtorUser("user", passwordEncoder.encode("1234"), "USER", "");

        List<DebtorUser> users = Arrays.asList(admin, manager, user);

        this.debtorUserRepo.saveAll(users);
    }
}
