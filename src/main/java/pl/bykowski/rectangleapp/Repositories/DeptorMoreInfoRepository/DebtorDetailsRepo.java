package pl.bykowski.rectangleapp.Repositories.DeptorMoreInfoRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.bykowski.rectangleapp.Repositories.DebtorRepository.Debtor;

import java.util.List;

public interface DebtorDetailsRepo extends CrudRepository<DebtorDetails, Long> {

//    @Query(nativeQuery = true, value = "SELECT * FROM DEBTOR debt WHERE debt.name = :name")
//        //zwraca wszystko z bazy gdzie wiersz zawiera konkretne imie
//    List<Debtor> isThisUserEgsist(@Param("name") String name);

    //zwraca liste wszystkich urzytkownikow o podanym imieniu
    @Query(nativeQuery = true, value = "SELECT * FROM DEBTORDETAILS debtd WHWRE debtd.name = : name")
    List<DebtorDetails> getAllDebtorsWithThatName(@Param("name") String name);

    //zwraca liste wszystkich dłużnikow
    @Query(nativeQuery = true, value = "SELECT * FROM DEBTORDETAILS")
    List<DebtorDetails> getAllDebtors();

//    //ustawia nowy total debt
//    @Modifying
//    @Query(nativeQuery = true, value = "UPDATE DEBTORDETAILS debtd SET debtd.total_debt = ? WHERE debtd.name = ?")
//    void setNewTotalDebtByNameTest(
//            @Param("newTotalDebt") float newTotalDebt,
//            @Param("name") String name);
}
