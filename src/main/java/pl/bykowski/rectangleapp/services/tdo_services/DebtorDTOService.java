package pl.bykowski.rectangleapp.services.tdo_services;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.model.dto.DebtorDTO;
import pl.bykowski.rectangleapp.services.DebtorDetailsService;
import pl.bykowski.rectangleapp.services.DebtorService;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DebtorDTOService {

    private static final Logger logger = Logger.getLogger(DebtorDTOService.class);
    private DebtorDetailsService debtorDetailsService;
    private DebtorService debtorService;

    public DebtorDTOService(DebtorDetailsService debtorDetailsService, DebtorService debtorService) {
        this.debtorDetailsService = debtorDetailsService;
        this.debtorService = debtorService;
    }

    public List<DebtorDTO> returnDebtorDTOList(List<Debtor> debtorList){
        List<DebtorDTO> debtorDTOList = new ArrayList<>();
        for(Debtor debtor : debtorList){
            debtorDTOList.add(new DebtorDTO(debtor.getId(), debtor.getName(), debtor.getTotalDebt()));
        }
        return debtorDTOList;
    }

    public DebtorDTO returnDebtorDTO(Debtor debtor){
        return new DebtorDTO(debtor.getId(), debtor.getName(), debtor.getTotalDebt());
    }

    public DebtorDTO returnDebtorDTOWithHighestCountOfDebts(Principal principal){
        ArrayList<DebtorDetails> debtorDetailsArrayList = (ArrayList<DebtorDetails>) debtorDetailsService.findByUserName(principal.getName());
        List<Long> debtorDetailsIdArrayList = new ArrayList<>();

        debtorDetailsArrayList.stream()
                .filter(debtor -> debtor.getId() != null)
                .forEach(debtor -> debtorDetailsIdArrayList.add(debtor.getDebtor().getId()));

        Map.Entry<Long, Long> idAndCountOfDebtsMap =
                debtorDetailsIdArrayList.stream()
                        .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                        .entrySet()
                        .stream()
                        .max(Comparator.comparing(Map.Entry::getValue))
                        .get();
        Long debtorId = idAndCountOfDebtsMap.getKey();

        Long countOfDebts = idAndCountOfDebtsMap.getValue();
        Optional<Debtor> debtor = debtorService.findById(debtorId);
        DebtorDTO debtorDTO = debtor.map(this::returnDebtorDTO).orElse(new DebtorDTO());
        debtorDTO.setCountOfDebts(countOfDebts);

        logger.debug("Id debtor with highest count of debts : " + debtorId + "\n" +
                "Count of debts : " + countOfDebts);
        return debtorDTO;
    }
}
