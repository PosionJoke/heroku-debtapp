package pl.bykowski.rectangleapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.repositories.DebtorDetailsRepo;
import pl.bykowski.rectangleapp.services.DebtorService;

import java.security.Principal;

@RestController
public class DebtorDetailsController {

    private DebtorService debtorService;
    private DebtorDetailsRepo debtorDetailsRepo;

    public DebtorDetailsController(DebtorService debtorService, DebtorDetailsRepo debtorDetailsRepo) {
        this.debtorService = debtorService;
        this.debtorDetailsRepo = debtorDetailsRepo;
    }

    @GetMapping("/debtor-details-list")
    public ModelAndView debtorDetailsList(Principal principal){
        return new ModelAndView("debtor-details-list")
                .addObject("debtorLIST", debtorDetailsRepo.findByUserName(principal.getName()));
    }
}
