package pl.bykowski.rectangleapp.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.repositories.DebtorRepo;
import pl.bykowski.rectangleapp.services.DebtorService;

import java.security.Principal;
import java.util.List;

@RestController
public class DebtorController {

    private DebtorRepo debtorRepo;
    private DebtorService debtorService;

    public DebtorController(DebtorRepo debtorRepo, DebtorService debtorService) {
        this.debtorRepo = debtorRepo;
        this.debtorService = debtorService;
    }

    @GetMapping("/debtor-list")
    public ModelAndView showDebtorList(Principal principal){
        return new ModelAndView("debtor-list")
                .addObject("debtors", debtorRepo.findByUserName(principal.getName()));
    }

    @GetMapping("/debtor-create")
    public ModelAndView createDebtor(Principal principal){
        return new ModelAndView("debtor-create")
                .addObject("debtor", new Debtor());
    }

    @GetMapping("/debtor-debt-edit")
    public ModelAndView debtorDebtEdit(@RequestParam Long id){
        return new ModelAndView("debtor-debt-edit")
                .addObject("debtor", debtorRepo.findById(id));
    }

    @PostMapping("/debtor-save")
    public ModelAndView saveDebtor(@ModelAttribute Debtor debtor, Principal principal){
        Debtor debtorNew = debtor;
        debtorRepo.save(debtor);
        return new ModelAndView("debtor-list")
                .addObject("debtors", debtorRepo.findByUserName(principal.getName()));
    }

}
