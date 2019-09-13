package pl.bykowski.rectangleapp.services.tdo;

import org.springframework.stereotype.Service;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.dto.DebtorDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class DebtorDTOService {
    public List<DebtorDTO> returnDebtorDTOList(List<Debtor> debtorList){
        List<DebtorDTO> debtorDTOList = new ArrayList<>();
        for(Debtor debtor : debtorList){
            debtorDTOList.add(new DebtorDTO(debtor.getId(), debtor.getName(), debtor.getTotalDebt(), debtor.getDebtorDetailsList().size()));
        }
        return debtorDTOList;
    }

    public DebtorDTO returnDebtorDTO(Debtor debtor){
        return new DebtorDTO(debtor.getId(), debtor.getName(), debtor.getTotalDebt(), debtor.getDebtorDetailsList().size());
    }
}
