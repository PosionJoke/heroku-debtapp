package pl.bykowski.rectangleapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pl.bykowski.rectangleapp.model.DebtorHistory;
import pl.bykowski.rectangleapp.model.dto.DebtorHistoryDTO;
import pl.bykowski.rectangleapp.services.DebtorHistoryService;
import pl.bykowski.rectangleapp.services.tdo_services.DebtorHistoryDTOService;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
public class DebtorHistoryController {

    private final DebtorHistoryService debtorHistoryService;
    private DebtorHistoryDTOService debtorHistoryDTOService;

    public DebtorHistoryController(DebtorHistoryDTOService debtorHistoryDTOService,
                                   DebtorHistoryService debtorHistoryService) {
        this.debtorHistoryDTOService = Objects.requireNonNull(debtorHistoryDTOService, "debtorHistoryDTOService must be not null");
        this.debtorHistoryService = Objects.requireNonNull(debtorHistoryService, "debtorHistoryService must be not null");
    }

    @GetMapping("/debtor-history-list")
    public ModelAndView showDebtorDetailsList(Principal principal) {

        List<DebtorHistory> debtorHistoryList = debtorHistoryService.findByUserName(principal.getName());
        List<DebtorHistoryDTO> debtorHistoryDTOS = debtorHistoryDTOService.returnDebtorHistoryDTOList(debtorHistoryList);
        return new ModelAndView("debtor-history-list")
                .addObject("debtors", debtorHistoryDTOS);
    }
}
