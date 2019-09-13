package pl.bykowski.rectangleapp.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.model.dto.DebtorDetailsDTO;
import pl.bykowski.rectangleapp.repositories.DebtorDetailsRepo;
import pl.bykowski.rectangleapp.services.DebtorDetailsService;
import pl.bykowski.rectangleapp.services.DebtorService;
import pl.bykowski.rectangleapp.services.tdo.DebtorDetailsDTOService;

import java.security.Principal;
import java.util.List;

@RestController
public class DebtorDetailsController {

    private DebtorDetailsRepo debtorDetailsRepo;
    private DebtorDetailsService debtorDetailsService;
    private DebtorDetailsDTOService debtorDetailsDTOService;
    private DebtorService debtorService;

    public DebtorDetailsController(DebtorDetailsRepo debtorDetailsRepo, DebtorDetailsService debtorDetailsService, DebtorDetailsDTOService debtorDetailsDTOService, DebtorService debtorService) {
        this.debtorDetailsDTOService = debtorDetailsDTOService;
        this.debtorDetailsService = debtorDetailsService;
        this.debtorDetailsRepo = debtorDetailsRepo;
        this.debtorService = debtorService;
    }

    @GetMapping("/debtor-details-list")
    public ModelAndView showDebtorDetailsList(Principal principal){
        List<DebtorDetails> debtorDetailsList = debtorDetailsRepo.findByUserName(principal.getName());
        List<DebtorDetailsDTO> debtorDetailsDTOList1 = debtorDetailsDTOService.returnDebtorDetailsDTOList(debtorDetailsList);
        return new ModelAndView("debtor-details-list")
                .addObject("debtorLIST", debtorDetailsDTOList1);
    }

    @GetMapping("/debtor-details-debt-edit")
    public ModelAndView editDebtorDetails(@RequestParam Long id, @RequestParam String name,
                                          @ModelAttribute DebtorDetails debtorDetailsForm){

        DebtorDetails debtorDetails = debtorDetailsRepo.findByNameAndId(name, id);
        return new ModelAndView("debtor-details-debt-edit")
                .addObject("name", name)
                .addObject("id", id)
                .addObject("debtor", debtorDetails);
    }

    @GetMapping("/make-new-debtor-details")
    public ModelAndView makeNewDebtorDetails(@RequestParam String name){
        return new ModelAndView("make-new-debtor-details")
                .addObject("debtorDetails", new DebtorDetailsDTO())
                .addObject("name", name);
    }

    @PostMapping("/make-new-debtor-details")
    public ModelAndView saveNewDebtorDetails(@ModelAttribute DebtorDetailsDTO debtorDetails, Principal principal, @RequestParam String name){
        Debtor debtor = debtorService.findDebtorByName(name);
        debtorService.updateTotalDebtAndMakeNewDebtorDetails(debtorDetails, debtor, principal.getName());
        return new ModelAndView("debtor-details-list")
                .addObject("debtorLIST", debtorDetailsRepo.findByUserName(principal.getName()));
    }

    @PostMapping("/debtor-details-save")
    public ModelAndView saveDebtorDetails(@ModelAttribute DebtorDetails debtor, Principal principal,
                                          @RequestParam Long id){
        debtorDetailsService.updateDebtorDetailsDebt(id,debtor.getDebt());
        return new ModelAndView("debtor-details-list")
                .addObject("debtorLIST", debtorDetailsRepo.findByUserName(principal.getName()));
    }
}
