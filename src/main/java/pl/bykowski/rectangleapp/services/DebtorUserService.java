package pl.bykowski.rectangleapp.services;

import org.springframework.stereotype.Service;
import pl.bykowski.rectangleapp.repositories.repo_interfaces.DebtorUserRepo;

@Service
public class DebtorUserService {

//    private DebtorUserRepo debtorUserRepo;

    public DebtorUserService(DebtorUserRepo debtorUserRepo) {
//        this.debtorUserRepo = debtorUserRepo;
    }
}
