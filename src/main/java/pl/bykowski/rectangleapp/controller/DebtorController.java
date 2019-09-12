package pl.bykowski.rectangleapp.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.model.dto.DebtorDTO;
import pl.bykowski.rectangleapp.repositories.DebtorRepo;
import pl.bykowski.rectangleapp.services.DebtorService;
import pl.bykowski.rectangleapp.services.tdo.DebtorDTOService;
import java.security.Principal;
import java.util.List;

@RestController
public class DebtorController {

    private DebtorRepo debtorRepo;
    private DebtorService debtorService;
    private DebtorDTOService debtorDTOService;

    public DebtorController(DebtorRepo debtorRepo, DebtorService debtorService, DebtorDTOService debtorDTOService) {
        this.debtorDTOService = debtorDTOService;
        this.debtorRepo = debtorRepo;
        this.debtorService = debtorService;
    }

    @GetMapping("/debtor-list")
    public ModelAndView showDebtorList(Principal principal){
        return new ModelAndView("debtor-list")
                .addObject("debtors", debtorRepo.findByUserName(principal.getName()));
    }

    @GetMapping("/debtor-create")
    public ModelAndView createDebtor(){
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
        Debtor debtorToUpdate = debtorRepo.findByName(name);
        float actualTotalDebt = debtorToUpdate.getTotalDebt();
        String actualUserName = principal.getName();
        debtorService.updateTotalDebt(name, actualTotalDebt, actualUserName);

        List<Debtor> debtorList = debtorRepo.findByUserName(principal.getName());
        List<DebtorDTO> debtorDTOList1 = debtorDTOService.returnDebtorDTOList(debtorList);
        return new ModelAndView("debtor-list")
                .addObject("debtors", debtorDTOList1);
    }

    @PostMapping("/make-new-debtor")
    public ModelAndView makeNewDebtor(@ModelAttribute DebtorDetails debtorDetails, Principal principal){
        debtorService.addNewDebtor(debtorDetails.getName(), debtorDetails.getDebt(), debtorDetails.getReasonForTheDebt(), principal.getName());

        List<Debtor> debtorList = debtorRepo.findByUserName(principal.getName());
        List<DebtorDTO> debtorDTOList1 = debtorDTOService.returnDebtorDTOList(debtorList);
        return new ModelAndView("debtor-list")
                .addObject("debtors", debtorDTOList1);
    }
}
