package pl.bykowski.rectangleapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.bykowski.rectangleapp.model.DebtorHistory;
import pl.bykowski.rectangleapp.model.dto.CurencyTypes;
import pl.bykowski.rectangleapp.model.dto.DebtorHistoryDTO;
import pl.bykowski.rectangleapp.services.CurrencyService;
import pl.bykowski.rectangleapp.services.DebtorHistoryService;
import pl.bykowski.rectangleapp.services.tdo_services.DebtorHistoryDTOService;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
public class DebtorHistoryController {

    private final DebtorHistoryService debtorHistoryService;
    private final CurrencyService currencyService;
    private DebtorHistoryDTOService debtorHistoryDTOService;

    public DebtorHistoryController(DebtorHistoryDTOService debtorHistoryDTOService,
                                   DebtorHistoryService debtorHistoryService, CurrencyService currencyService) {
        this.debtorHistoryDTOService = Objects.requireNonNull(debtorHistoryDTOService, "debtorHistoryDTOService must be not null");
        this.debtorHistoryService = Objects.requireNonNull(debtorHistoryService, "debtorHistoryService must be not null");
        this.currencyService = Objects.requireNonNull(currencyService, "currencyService must be not null");
    }

    @GetMapping("/debtor-history-list")
    public ModelAndView showDebtorDetailsList(Principal principal,
                                              @RequestParam(required = false, defaultValue = "PLN") String currency) {

        List<DebtorHistory> debtorHistoryList = debtorHistoryService.findByUserName(principal.getName());
        List<DebtorHistoryDTO> debtorHistoryDTOS = debtorHistoryDTOService.returnDebtorHistoryDTOList(debtorHistoryList);

        String currencyRate = currencyService.calculateCurrencyRates(currency, "PLN");

        List<DebtorHistoryDTO> debtorHistoryDTOSWithUpdateCurrencyRates =
                currencyService.testGeneric(debtorHistoryDTOS, currencyRate);

        return new ModelAndView("debtor-history-list")
                .addObject("debtors", debtorHistoryDTOSWithUpdateCurrencyRates)
                .addObject("currencyTypes", CurencyTypes.values())
                .addObject("currency", currency);
    }
}
