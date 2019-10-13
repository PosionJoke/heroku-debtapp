package pl.bykowski.rectangleapp.services.tdo_services;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.model.dto.DebtorDetailsDTO;

import java.util.ArrayList;
import java.util.List;

@Log4j
@Service
public class DebtorDetailsDTOService {

    public List<DebtorDetailsDTO> returnDebtorDetailsDTOList(List<DebtorDetails> debtorList){
        List<DebtorDetailsDTO> debtorDetailsDTOList = new ArrayList<>();
        for(DebtorDetails debtorDetails : debtorList){
            debtorDetailsDTOList.add(new DebtorDetailsDTO(debtorDetails.getId(), debtorDetails.getName(), debtorDetails.getDebt(), debtorDetails.getDate(), debtorDetails.getReasonForTheDebt()));
        }
        log.debug("Size of DebtorDetailsDTOList : " + debtorDetailsDTOList.size() + "\n" +
                "Size of input List : " + debtorList.size());
        return debtorDetailsDTOList;
    }

    public DebtorDetailsDTO returnDebtorDetailsDTO(DebtorDetails debtorDetails){
        return new DebtorDetailsDTO(debtorDetails.getId(),debtorDetails.getName(),
                debtorDetails.getDebt(), debtorDetails.getDate(), debtorDetails.getReasonForTheDebt());
    }
}
