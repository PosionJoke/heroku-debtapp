package pl.bykowski.rectangleapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pl.bykowski.rectangleapp.model.DebtorHistory;
import pl.bykowski.rectangleapp.model.dto.DebtorHistoryDTO;
import pl.bykowski.rectangleapp.repositories.DebtorHistoryRepo;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class DebtorHistoryController {

    private DebtorHistoryRepo debtorHistoryRepo;

    public DebtorHistoryController(DebtorHistoryRepo debtorHistoryRepo) {
        this.debtorHistoryRepo = debtorHistoryRepo;
    }

    @GetMapping("/debtor-history-list")
    public ModelAndView showDebtorDetailsList(Principal principal){

        List<DebtorHistoryDTO> debtorHistoryDTOList = new ArrayList<>();
        for(DebtorHistory debtorHistory : debtorHistoryRepo.findByUserName(principal.getName())){
            debtorHistoryDTOList.add(new DebtorHistoryDTO(debtorHistory.getName(), debtorHistory.getDebt(), debtorHistory.getTimeOfDebt(), debtorHistory.getReasonForTheDebt()));
        }

        return new ModelAndView("debtor-history-list")
                .addObject("debtors", debtorHistoryDTOList);
    }
}
