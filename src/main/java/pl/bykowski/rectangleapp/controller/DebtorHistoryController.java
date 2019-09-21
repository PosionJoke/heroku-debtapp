package pl.bykowski.rectangleapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pl.bykowski.rectangleapp.model.DebtorHistory;
import pl.bykowski.rectangleapp.model.dto.DebtorHistoryDTO;
import pl.bykowski.rectangleapp.repositories.DebtorHistoryRepo;
import pl.bykowski.rectangleapp.services.DebtorHistoryService;
import pl.bykowski.rectangleapp.services.tdo.DebtorHistoryDTOService;

import java.security.Principal;
import java.util.List;

@RestController
public class DebtorHistoryController {

    private final DebtorHistoryRepo debtorHistoryRepo;
    private final DebtorHistoryService debtorHistoryService;
    private DebtorHistoryDTOService debtorHistoryDTOService;

    public DebtorHistoryController(DebtorHistoryRepo debtorHistoryRepo,
                                   DebtorHistoryDTOService debtorHistoryDTOService,
                                   DebtorHistoryService debtorHistoryService) {
        this.debtorHistoryRepo = debtorHistoryRepo;
        this.debtorHistoryDTOService = debtorHistoryDTOService;
        this.debtorHistoryService = debtorHistoryService;
    }

    @GetMapping("/debtor-history-list")
    public ModelAndView showDebtorDetailsList(Principal principal) {

        List<DebtorHistory> debtorHistoryList = debtorHistoryService.findByUserName(principal.getName());
        List<DebtorHistoryDTO> debtorHistoryDTOS = debtorHistoryDTOService.returnDebtorHistoryDTOList(debtorHistoryList);
        return new ModelAndView("debtor-history-list")
                .addObject("debtors", debtorHistoryDTOS);
    }
}
