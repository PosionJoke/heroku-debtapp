package pl.bykowski.rectangleapp.services.tdo;

import org.springframework.stereotype.Service;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.model.dto.DebtorDTO;
import pl.bykowski.rectangleapp.services.DebtorDetailsService;
import pl.bykowski.rectangleapp.services.DebtorService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DebtorDTOService {

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
        ArrayList<Long> debtorDetailsIdArrayList = new ArrayList<>();
        for(DebtorDetails debtor : debtorDetailsArrayList) {
            if (debtor.getDebtor().getId() != null) {
                debtorDetailsIdArrayList.add(debtor.getDebtor().getId());
            }
        }
        Map.Entry<Long, Long> idAndCountOfDebtsMap =
                debtorDetailsIdArrayList.stream()
                        .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                        .entrySet()
                        .stream()
                        .max(Comparator.comparing(Map.Entry::getValue))
                        .get();
        Long debtorId = idAndCountOfDebtsMap.getKey();
        Long countOfDebts = idAndCountOfDebtsMap.getValue();
        Debtor debtor = debtorService.findById(debtorId);
        DebtorDTO debtorDTO = returnDebtorDTO(debtor);
        debtorDTO.setCountOfDebts(countOfDebts);
        return debtorDTO;
    }
}
