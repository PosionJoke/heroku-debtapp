package pl.bykowski.rectangleapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.dto.DebtorDTO;
import pl.bykowski.rectangleapp.services.DebtorDetailsService;
import pl.bykowski.rectangleapp.services.DebtorService;
import pl.bykowski.rectangleapp.services.tdo.DebtorDTOService;

import java.security.Principal;

@RestController
public class MainViewController {

    private DebtorService debtorService;
    private DebtorDTOService debtorDTOService;
    private DebtorDetailsService debtorDetailsService;

    public MainViewController(DebtorService debtorService, DebtorDTOService debtorDTOService, DebtorDetailsService debtorDetailsService) {
        this.debtorService = debtorService;
        this.debtorDTOService = debtorDTOService;
        this.debtorDetailsService = debtorDetailsService;
    }

    @GetMapping("/")
    public ModelAndView showMainViewTemporaryMethod(Principal principal){
        if(debtorDetailsService.findByUserName(principal.getName()).size() > 0){
            Debtor debtorWithBiggestDebt = debtorService.returnDebtorWithBiggestDebt(principal);
            DebtorDTO debtorWithBiggestDebtDTO = debtorDTOService.returnDebtorDTO(debtorWithBiggestDebt);
            DebtorDTO debtorWithHighestCountOfDebtsDTO = debtorDTOService.returnDebtorDTOWithHighestCountOfDebts(principal);
            return new ModelAndView("main-view")
                    .addObject("user", principal)
                    .addObject("debtorWithBiggestDebt", debtorWithBiggestDebtDTO)
                    .addObject("debtorWithHighestCountOfDebts", debtorWithHighestCountOfDebtsDTO);
        }else return new ModelAndView("main-view-new-user")
                .addObject("user", principal);
    }

    @GetMapping("/main-view")
    public ModelAndView showMainView(Principal principal){
        if(debtorDetailsService.findByUserName(principal.getName()).size() > 0){
            Debtor debtorWithBiggestDebt = debtorService.returnDebtorWithBiggestDebt(principal);
            DebtorDTO debtorWithBiggestDebtDTO = debtorDTOService.returnDebtorDTO(debtorWithBiggestDebt);
            DebtorDTO debtorWithHighestCountOfDebtsDTO = debtorDTOService.returnDebtorDTOWithHighestCountOfDebts(principal);
            return new ModelAndView("main-view")
                    .addObject("user", principal)
                    .addObject("debtorWithBiggestDebt", debtorWithBiggestDebtDTO)
                    .addObject("debtorWithHighestCountOfDebts", debtorWithHighestCountOfDebtsDTO);
        }else return new ModelAndView("main-view-new-user")
                .addObject("user", principal);
    }
}
