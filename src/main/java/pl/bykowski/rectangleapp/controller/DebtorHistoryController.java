package pl.bykowski.rectangleapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pl.bykowski.rectangleapp.repositories.DebtorHistoryRepo;
import pl.bykowski.rectangleapp.services.DebtorService;

import java.security.Principal;

@RestController
public class DebtorHistoryController {

    private DebtorHistoryRepo debtorHistoryRepo;
    private DebtorService debtorService;

    public DebtorHistoryController(DebtorHistoryRepo debtorHistoryRepo, DebtorService debtorService) {
        this.debtorHistoryRepo = debtorHistoryRepo;
        this.debtorService = debtorService;
    }

    @GetMapping("/debtor-history-list")
    public ModelAndView findDebtorHistoryList(Principal principal){
        return new ModelAndView("debtor-history-list")
                .addObject("debtors", debtorHistoryRepo.findByUserName(principal.getName()));
    }
}
