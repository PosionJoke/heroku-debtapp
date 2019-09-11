package pl.bykowski.rectangleapp.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorDetails;
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

    @GetMapping("/debtor-details-debt-edit")
    public ModelAndView debtorDebtEdit(@RequestParam Long id, @RequestParam String name,
                                       @ModelAttribute DebtorDetails debtorDetailsForm){

        DebtorDetails debtorDetails = debtorDetailsRepo.findByNameAndId(name, id);
        return new ModelAndView("debtor-details-debt-edit")
                .addObject("name", name)
                .addObject("id", id)
                .addObject("debtor", debtorDetails);
    }

    @PostMapping("/debtor-details-save")
    public ModelAndView saveDebtor(@ModelAttribute DebtorDetails debtor, Principal principal,
                                   @RequestParam Long id){
        DebtorDetails debtorNew = debtor;
        debtorService.updateDebtByNewDebt(id,debtor.getDebt());
        return new ModelAndView("debtor-details-list")
                .addObject("debtorLIST", debtorDetailsRepo.findByUserName(principal.getName()));
    }
}
