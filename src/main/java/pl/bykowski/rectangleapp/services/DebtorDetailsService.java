package pl.bykowski.rectangleapp.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.repositories.DebtorDetailsRepo;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class DebtorDetailsService {

    private DebtorDetailsRepo debtorDetailsRepo;
    private DebtorHistoryService debtorHistoryService;

    public DebtorDetailsService(DebtorDetailsRepo debtorDetailsRepo, DebtorHistoryService debtorHistoryService) {
        this.debtorDetailsRepo = debtorDetailsRepo;
        this.debtorHistoryService = debtorHistoryService;
    }

    private void saveDebtorDetails(DebtorDetails debtorDetails){
        debtorDetailsRepo.save(debtorDetails);
    }

     public void addNewDebtorDetails(String debtorName, float debtValue, String reasonForTheDebt, String userName, Debtor debtor) {
        DebtorDetails debtorDetails = new DebtorDetails();
        debtorDetails.setName(debtorName);
        debtorDetails.setDebt(debtValue);
        debtorDetails.setDate(LocalDate.now());
        debtorDetails.setReasonForTheDebt(reasonForTheDebt);
        debtorDetails.setUserName(userName);
        debtorDetails.setDebtor(debtor);

        saveDebtorDetails(debtorDetails);
    }

    @Transactional
    public void updateDebtorDetailsDebt(Long debtID, float debtValue) {
        Optional<DebtorDetails> debtorDetails = debtorDetailsRepo.findById(debtID);
        if(debtorDetails.isPresent()){
            DebtorDetails debtorDetails1 = debtorDetails.get();
            isThisDebtUnderZero(debtorDetails1, debtValue);
        }
    }

    private void isThisDebtUnderZero(DebtorDetails debtorDetails, float debtValue){
        float newDebt = debtorDetails.getDebt() + debtValue;
        if(newDebt <= 0){
            debtorDetails.setDebt(0);
            deleteDebtByID(debtorDetails.getName(), debtorDetails.getId());
        }else {
            debtorDetails.setDebt(newDebt);
            debtorDetailsRepo.save(debtorDetails);
        }
    }

    @Transactional
    public void deleteDebtByID(String debtorName, Long debtorID) {
        DebtorDetails debtorDetailsCopy = debtorDetailsRepo.findByNameAndId(debtorName, debtorID);

        debtorHistoryService.saveEntityDebtorHistory(debtorDetailsCopy);

        debtorDetailsRepo.delete(debtorDetailsRepo.findByNameAndId(debtorName, debtorID));
    }
}
