package pl.bykowski.rectangleapp.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.model.CurrencyTypes;
import pl.bykowski.rectangleapp.model.dto.DebtorDetailsDTO;
import pl.bykowski.rectangleapp.services.CurrencyService;
import pl.bykowski.rectangleapp.services.DebtorDetailsService;
import pl.bykowski.rectangleapp.services.DebtorService;
import pl.bykowski.rectangleapp.services.tdo_services.DebtorDetailsDTOService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Log4j
@Controller
public class DebtorDetailsController {

    private final DebtorDetailsService debtorDetailsService;
    private final DebtorService debtorService;
    private final CurrencyService currencyService;
    private final DebtorDetailsDTOService debtorDetailsDTOService;

    public DebtorDetailsController(DebtorDetailsService debtorDetailsService,
                                   DebtorDetailsDTOService debtorDetailsDTOService, DebtorService debtorService,
                                   CurrencyService currencyService) {
        this.debtorDetailsDTOService = Objects.requireNonNull(debtorDetailsDTOService,
                "debtorDetailsDTOService must be not null");
        this.debtorDetailsService = Objects.requireNonNull(debtorDetailsService,
                "debtorDetailsService must be not null");
        this.debtorService = Objects.requireNonNull(debtorService, "debtorService must be not null");
        this.currencyService = Objects.requireNonNull(currencyService, "currencyService must be not null");
    }

    @GetMapping("/debtor-details-list")
    public ModelAndView showDebtorDetailsList(Principal principal,
                                              @RequestParam(required = false, defaultValue = "PLN") String currency) {

        List<DebtorDetails> debtorDetailsList = debtorDetailsService.findByUserName(principal.getName());
        //TODO WHY this variable is named debtorDetailsDTOList1
        List<DebtorDetailsDTO> debtorDetailsDTOList1 = debtorDetailsDTOService.returnDebtorDetailsDTOList(debtorDetailsList);

        String currencyRate = currencyService.calculateCurrencyRates(currency, "PLN");

        List<DebtorDetailsDTO> debtorDetailsDTOWithCurrencyRate = currencyService.setCurrencyRates(debtorDetailsDTOList1, currencyRate);

        return new ModelAndView("debtor-details-list")
                .addObject("debtorDetailsDTOList", debtorDetailsDTOWithCurrencyRate)
                .addObject("currencyTypes", CurrencyTypes.values())
                .addObject("currency", currency);
    }

    @GetMapping("/debtor-details-debt-edit")
    public ModelAndView editDebtorDetails(@RequestParam Long id) {

        DebtorDetails debtorDetailsOpt = debtorDetailsService.findById(id);

        DebtorDetailsDTO debtorDetailsDTO = debtorDetailsDTOService.returnDebtorDetailsDTO(debtorDetailsOpt);

        return new ModelAndView("debtor-details-debt-edit")
                .addObject("id", id)
                .addObject("debtorDetailsDTO", debtorDetailsDTO);
    }

    @GetMapping("/make-new-debtor-details")
    public ModelAndView makeNewDebtorDetails(@RequestParam String name,
                                             @RequestParam(required = false, defaultValue = "PLN") String currency) {
        return new ModelAndView("make-new-debtor-details")
                .addObject("currency", currency)
                .addObject("currencyTypes", CurrencyTypes.values())
                .addObject("debtorDetailsDTO", new DebtorDetailsDTO())
                .addObject("name", name);
    }

    @GetMapping("/debtor-details-delete")
    public ModelAndView deleteDebtorDetails(@RequestParam Long id) {
        return new ModelAndView("debtor-details-delete")
                .addObject("id", id);
    }

    @GetMapping("/debtor-details-delete-by-id")
    public ModelAndView deleteDebtorDetailsById(@RequestParam Long id, Principal principal) {

        debtorService.deleteDebtorDetailsUpdateTotalDebtMakeNewDebtorHistory(id);

        List<DebtorDetails> debtorDetailsList = debtorDetailsService.findByUserName(principal.getName());
        List<DebtorDetailsDTO> debtorDetailsDTOList = debtorDetailsDTOService.returnDebtorDetailsDTOList(debtorDetailsList);
        return new ModelAndView("debtor-details-list")
                .addObject("debtorDetailsDTOList", debtorDetailsDTOList);
    }

    @PostMapping("/make-new-debtor-details")
    public ModelAndView saveNewDebtorDetails(@Valid @ModelAttribute DebtorDetailsDTO debtorDetailsDTO,
                                             BindingResult bindingResult,
                                             Principal principal,
                                             @RequestParam String name,
                                             @RequestParam(required = false, defaultValue = "PLN") String currency) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("make-new-debtor-details", bindingResult.getModel())
                    .addObject("name", debtorDetailsDTO.getName());
        }

        Debtor debtor = debtorService.findDebtorByName(name);

        debtorService.updateTotalDebtAndMakeNewDebtorDetails(debtorDetailsDTO, debtor, principal.getName());
        List<DebtorDetails> debtorDetailsList = debtorDetailsService.findByUserName(principal.getName());
        List<DebtorDetailsDTO> debtorDetailsDTOList = debtorDetailsDTOService.returnDebtorDetailsDTOList(debtorDetailsList);

        return new ModelAndView("debtor-details-list")
                .addObject("currency", currency)
                .addObject("currencyTypes", CurrencyTypes.values())
                .addObject("debtorDetailsDTOList", debtorDetailsDTOList);
    }

    @PostMapping("/debtor-details-save")
    public ModelAndView saveDebtorDetails(@ModelAttribute DebtorDetailsDTO debtorDetailsDTO, Principal principal,
                                          @RequestParam Long id) {

        debtorService.updateTotalDebtAndUpdateDebtorDetailsDebt(debtorDetailsDTO, id);

        List<DebtorDetails> debtorDetailsList = debtorDetailsService.findByUserName(principal.getName());
        List<DebtorDetailsDTO> debtorDetailsDTOList = debtorDetailsDTOService.returnDebtorDetailsDTOList(debtorDetailsList);

        return new ModelAndView("debtor-details-list")
                .addObject("debtorDetailsDTOList", debtorDetailsDTOList);
    }
}
