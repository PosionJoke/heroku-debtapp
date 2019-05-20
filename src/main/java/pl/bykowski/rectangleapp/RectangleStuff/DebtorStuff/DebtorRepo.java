package pl.bykowski.rectangleapp.RectangleStuff.DebtorStuff;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.bykowski.rectangleapp.RectangleStuff.Rectangle;

import java.util.List;

@Repository
public interface DebtorRepo extends CrudRepository<Debtor, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM DEBTOR debt WHERE debt.name = :name")
    //zwraca wszystko z bazy gdzie wiersz zawiera konkretne imie
    List<Debtor> isThisUserEgsist(@Param("name") String name);

    @Query(nativeQuery = true, value = "SELECT * FROM DEBTOR")
    //zwraca WSZYSTKICH z bazy
    List<Debtor> returnAllDebtors();

    @Query(nativeQuery = true, value = "SELECT * FROM DEBTOR debt WHERE debt.name = :name")
    //zwraca jeden wynik ktory odpowiada podanemu imieniu
    List<Debtor> getDebtorByName(@Param("name") String name);

    @Query(nativeQuery = true, value = "SELECT TOTAL_DEBT FROM DEBTOR debt WHERE debt.name = :name")
    //zwraca totalDebt szukajac po imieniu
    float getTotalDebt(@Param("name") String name);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE DEBTOR debt SET debt.total_debt = ? WHERE debt.name = ?")
    void setNewTotalDebtByNameTest(
            @Param("newTotalDebt") float newTotalDebt,
            @Param("name") String name);
}
