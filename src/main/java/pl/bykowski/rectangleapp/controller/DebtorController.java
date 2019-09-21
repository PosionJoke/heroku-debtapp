package pl.bykowski.rectangleapp.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.dto.DebtorDTO;
import pl.bykowski.rectangleapp.model.dto.DebtorDetailsDTO;
import pl.bykowski.rectangleapp.repositories.DebtorRepo;
import pl.bykowski.rectangleapp.services.DebtorService;
import pl.bykowski.rectangleapp.services.tdo.DebtorDTOService;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
public class DebtorController {
    private final DebtorRepo debtorRepo;
    private final DebtorService debtorService;
    private DebtorDTOService debtorDTOService;

    public DebtorController(DebtorRepo debtorRepo, DebtorService debtorService, DebtorDTOService debtorDTOService) {
        this.debtorDTOService = Objects.requireNonNull(debtorDTOService, "debtorDTOService must be not null");
        this.debtorRepo = Objects.requireNonNull(debtorRepo, "debtorRepo must be not null");
        this.debtorService = Objects.requireNonNull(debtorService, "debtorService must be not null");
    }

    @GetMapping("/debtor-list")
    public ModelAndView showDebtorList(Principal principal) {
        return new ModelAndView("debtor-list")
                .addObject("debtors", debtorService.findByUserName(principal.getName()));
    }

    @GetMapping("/debtor-create")
    public ModelAndView createDebtor() {
        return new ModelAndView("debtor-create")
                .addObject("debtor", new DebtorDetailsDTO());
    }

    @GetMapping("/debtor-debt-edit")
    public ModelAndView debtorDebtEdit(@RequestParam Long id, @RequestParam String name) {
        Debtor debtor = debtorRepo.findByName(name);
        DebtorDTO debtorDTO = debtorDTOService.returnDebtorDTO(debtor);
        return new ModelAndView("debtor-debt-edit")
                .addObject("name", name)
                .addObject("id", id)
                .addObject("debtor", debtorDTO);
    }

    @PostMapping("/debtor-save")
    public ModelAndView saveDebtor(@ModelAttribute DebtorDTO debtorDTO, Principal principal,
                                   @RequestParam String name, @RequestParam Long id) {
        Debtor debtorToUpdate = debtorRepo.findByName(name);
        float actualTotalDebt = debtorToUpdate.getTotalDebt();
        String actualUserName = principal.getName();
        debtorService.updateTotalDebt(id, actualTotalDebt, actualUserName);

        List<Debtor> debtorList = debtorService.findByUserName(principal.getName());
        List<DebtorDTO> debtorDTOList1 = debtorDTOService.returnDebtorDTOList(debtorList);
        return new ModelAndView("debtor-list")
                .addObject("debtors", debtorDTOList1);
    }

    @PostMapping("/make-new-debtor")
    public ModelAndView makeNewDebtor(@ModelAttribute DebtorDetailsDTO debtorDetailsDTO, Principal principal) {
        debtorService.addNewDebtor(debtorDetailsDTO.getName(), debtorDetailsDTO.getDebt(), debtorDetailsDTO.getReasonForTheDebt(), principal.getName());

        List<Debtor> debtorList = debtorService.findByUserName(principal.getName());
        List<DebtorDTO> debtorDTOList1 = debtorDTOService.returnDebtorDTOList(debtorList);
        return new ModelAndView("debtor-list")
                .addObject("debtors", debtorDTOList1);
    }
}
