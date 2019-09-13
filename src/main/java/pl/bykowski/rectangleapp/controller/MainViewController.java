package pl.bykowski.rectangleapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.dto.DebtorDTO;
import pl.bykowski.rectangleapp.services.DebtorService;
import pl.bykowski.rectangleapp.services.tdo.DebtorDTOService;

import java.security.Principal;

@RestController
public class MainViewController {

    private DebtorService debtorService;
    private DebtorDTOService debtorDTOService;

    public MainViewController(DebtorService debtorService, DebtorDTOService debtorDTOService) {
        this.debtorService = debtorService;
        this.debtorDTOService = debtorDTOService;
    }

    @GetMapping("/")
    public ModelAndView showMainViewTemporaryMethod(Principal principal){
        Debtor debtorWithBiggestDebt = debtorService.returnDebtorWithBiggestDebt(principal);
        DebtorDTO debtorWithBiggestDebtDTO = debtorDTOService.returnDebtorDTO(debtorWithBiggestDebt);
        DebtorDTO debtorWithHighestCountOfDebtsDTO = debtorDTOService.returnDebtorDTOWithHighestCountOfDebts(principal);
        return new ModelAndView("main-view")
                .addObject("user", principal)
                .addObject("debtorWithBiggestDebt", debtorWithBiggestDebtDTO)
                .addObject("debtorWithHighestCountOfDebts", debtorWithHighestCountOfDebtsDTO);
    }

    @GetMapping("/main-view")
    public ModelAndView showMainView(Principal principal){
        Debtor debtorWithBiggestDebt = debtorService.returnDebtorWithBiggestDebt(principal);
        DebtorDTO debtorWithBiggestDebtDTO = debtorDTOService.returnDebtorDTO(debtorWithBiggestDebt);
        DebtorDTO debtorWithHighestCountOfDebtsDTO = debtorDTOService.returnDebtorDTOWithHighestCountOfDebts(principal);
        return new ModelAndView("main-view")
                .addObject("user", principal)
                .addObject("debtorWithBiggestDebt", debtorWithBiggestDebtDTO)
                .addObject("debtorWithHighestCountOfDebts", debtorWithHighestCountOfDebtsDTO);
    }
}
