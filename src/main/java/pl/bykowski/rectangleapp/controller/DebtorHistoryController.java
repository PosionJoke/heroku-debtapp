package pl.bykowski.rectangleapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pl.bykowski.rectangleapp.repositories.DebtorHistoryRepo;

import java.security.Principal;

@RestController
public class DebtorHistoryController {

    private DebtorHistoryRepo debtorHistoryRepo;

    public DebtorHistoryController(DebtorHistoryRepo debtorHistoryRepo) {
        this.debtorHistoryRepo = debtorHistoryRepo;
    }

    @GetMapping("/debtor-history-list")
    public ModelAndView showDebtorDetailsList(Principal principal){
        return new ModelAndView("debtor-history-list")
                .addObject("debtors", debtorHistoryRepo.findByUserName(principal.getName()));
    }
}
