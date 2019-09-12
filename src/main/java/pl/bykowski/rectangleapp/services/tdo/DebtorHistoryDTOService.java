package pl.bykowski.rectangleapp.services.tdo;

import org.springframework.stereotype.Service;
import pl.bykowski.rectangleapp.model.DebtorHistory;
import pl.bykowski.rectangleapp.model.dto.DebtorHistoryDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class DebtorHistoryDTOService {
    public List<DebtorHistoryDTO> returnDebtorHistoryDTOList(List<DebtorHistory> debtorHistoryList){
        List<DebtorHistoryDTO> debtorHistoryDTOList = new ArrayList<>();
        for(DebtorHistory debtorHistory : debtorHistoryList){
            debtorHistoryDTOList.add(new DebtorHistoryDTO(debtorHistory.getName(), debtorHistory.getDebt(), debtorHistory.getTimeOfDebt(), debtorHistory.getReasonForTheDebt()));
        }
        return debtorHistoryDTOList;
    }
}
