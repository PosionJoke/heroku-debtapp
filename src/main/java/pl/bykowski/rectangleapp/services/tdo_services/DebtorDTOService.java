package pl.bykowski.rectangleapp.services.tdo_services;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.model.dto.DebtorDTO;
import pl.bykowski.rectangleapp.services.DebtorDetailsService;
import pl.bykowski.rectangleapp.services.DebtorService;

import java.util.*;
import java.util.stream.Collectors;

@Log4j
@Service
public class DebtorDTOService {

    private final DebtorDetailsService debtorDetailsService;
    private final DebtorService debtorService;

    public DebtorDTOService(DebtorDetailsService debtorDetailsService, DebtorService debtorService) {
        this.debtorDetailsService = debtorDetailsService;
        this.debtorService = debtorService;
    }

    public List<DebtorDTO> returnDebtorDTOList(List<Debtor> debtorList) {
        List<DebtorDTO> debtorDTOList = new ArrayList<>();
        for (Debtor debtor : debtorList) {
            debtorDTOList.add(
                    DebtorDTO.builder()
                    .id(debtor.getId())
                    .name(debtor.getName())
                    .totalDebt(debtor.getTotalDebt())
                    .build()
            );
        }
        return debtorDTOList;
    }

    public DebtorDTO returnDebtorDTO(Debtor debtor) {
        return DebtorDTO.builder()
                .id(debtor.getId())
                .name(debtor.getName())
                .totalDebt(debtor.getTotalDebt())
                .build();
    }

    public DebtorDTO returnDebtorDTOWithHighestCountOfDebts(String userName) {
        ArrayList<DebtorDetails> debtorDetailsArrayList = (ArrayList<DebtorDetails>) debtorDetailsService.findByUserName(userName);
        List<Long> debtorDetailsIdArrayList = new ArrayList<>();

        debtorDetailsArrayList.stream()
                .filter(debtor -> debtor.getId() != null)
                .forEach(debtor -> debtorDetailsIdArrayList.add(debtor.getDebtor().getId()));

        //TODO why there is using .get() like that?
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

        log.debug(String.format("Id debtor with highest count of debts : [%s], count of debts : [%s]",
                debtorId, countOfDebts));

        return debtorDTO;
    }
}
