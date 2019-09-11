package pl.bykowski.rectangleapp.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.repositories.DebtorDetailsRepo;
import pl.bykowski.rectangleapp.repositories.DebtorRepo;
import pl.bykowski.rectangleapp.services.DebtorService;

import java.security.Principal;
import java.util.List;

@RestController
public class DebtorController {

    private DebtorRepo debtorRepo;
    private DebtorService debtorService;
    private DebtorDetailsRepo debtorDetailsRepo;

    public DebtorController(DebtorRepo debtorRepo, DebtorService debtorService, DebtorDetailsRepo debtorDetailsRepo) {
        this.debtorRepo = debtorRepo;
        this.debtorService = debtorService;
        this.debtorDetailsRepo = debtorDetailsRepo;
    }

    @GetMapping("/debtor-list")
    public ModelAndView showDebtorList(Principal principal){
        return new ModelAndView("debtor-list")
                .addObject("debtors", debtorRepo.findByUserName(principal.getName()));
    }

    @GetMapping("/debtor-create")
    public ModelAndView createDebtor(Principal principal){
        return new ModelAndView("debtor-create")
                .addObject("debtor", new DebtorDetails());
    }

    @GetMapping("/debtor-debt-edit")
    public ModelAndView debtorDebtEdit(@RequestParam Long id, @RequestParam String name){
        Debtor debtor = debtorRepo.findByName(name);
        return new ModelAndView("debtor-debt-edit")
                .addObject("name", name)
                .addObject("id", id)
                .addObject("debtor", debtor);
    }

    @PostMapping("/debtor-save")
    public ModelAndView saveDebtor(@ModelAttribute Debtor debtor, Principal principal,
                                   @RequestParam String name){
        Debtor debtorNew = debtor;
        Debtor debtor1 = debtorRepo.findByName(name);
        debtorService.checkTotalDebtIsUnderZero(debtor1,debtor.getTotalDebt());
        return new ModelAndView("debtor-list")
                .addObject("debtors", debtorRepo.findByUserName(principal.getName()));
    }

    @PostMapping("/make-new-debtor")
    public ModelAndView makeNewDebtor(@ModelAttribute DebtorDetails debtorDetails, Principal principal,
                                      @RequestParam String name){
        debtorService.addNewDebtor(debtorDetails.getName(), debtorDetails.getDebt(), debtorDetails.getReasonForTheDebt(), principal.getName());
        return new ModelAndView("debtor-list")
                .addObject("debtors", debtorRepo.findByUserName(principal.getName()));
    }
}
