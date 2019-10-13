package pl.bykowski.rectangleapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.dto.DebtorDTO;
import pl.bykowski.rectangleapp.services.DebtorDetailsService;
import pl.bykowski.rectangleapp.services.DebtorService;
import pl.bykowski.rectangleapp.services.tdo_services.DebtorDTOService;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@Controller
public class MainViewController {

    private final DebtorService debtorService;
    private final DebtorDetailsService debtorDetailsService;
    private DebtorDTOService debtorDTOService;

    public MainViewController(DebtorService debtorService, DebtorDTOService debtorDTOService, DebtorDetailsService debtorDetailsService) {
        this.debtorService = Objects.requireNonNull(debtorService, "debtorService must be not null");
        this.debtorDTOService = Objects.requireNonNull(debtorDTOService, "debtorDTOService must be not null");
        this.debtorDetailsService = Objects.requireNonNull(debtorDetailsService, "debtorDetailsService must be not null");
    }


    @GetMapping("/main-view")
    public ModelAndView showMainView(Principal principal) {
        return returnMainView(principal);
    }

    @PostMapping("/main-view")
    public ModelAndView showMainViewPost(Principal principal) {
        return returnMainView(principal);
    }

    private ModelAndView returnMainView(Principal principal) {
        if (isThisUserHaveAnyDebtorDetails(principal)) {
            return new ModelAndView("main-view-new-user")
                    .addObject("user", principal);
        } else {
            Optional<Debtor> debtorWithBiggestDebt = debtorService.returnDebtorWithBiggestDebt(principal.getName());

            DebtorDTO debtorWithBiggestDebtDTO = debtorWithBiggestDebt.map(debtor -> debtorDTOService.returnDebtorDTO(debtor))
                    .orElse(new DebtorDTO());
            DebtorDTO debtorWithHighestCountOfDebtsDTO = debtorDTOService.returnDebtorDTOWithHighestCountOfDebts(principal.getName());
            return new ModelAndView("main-view")
                    .addObject("user", principal)
                    .addObject("debtorWithBiggestDebt", debtorWithBiggestDebtDTO)
                    .addObject("debtorWithHighestCountOfDebts", debtorWithHighestCountOfDebtsDTO);
        }
    }

    private boolean isThisUserHaveAnyDebtorDetails(Principal principal) {
        return debtorDetailsService.findByUserName(principal.getName()).isEmpty();
    }
}
