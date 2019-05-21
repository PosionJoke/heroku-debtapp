package pl.bykowski.rectangleapp.RectangleStuff;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RectangleRepo extends CrudRepository<Rectangle, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM RECTANGLE rect WHERE (2*rect.height + 2*rect.width) >= 20")
    //metoda zwraca liste rectangli ktore sa durze
    List<Rectangle> getBigRectangles();

    @Query(nativeQuery = true, value = "SELECT * FROM RECTANGLE rect WHERE (2*rect.height + 2*rect.width) <= 20")
    List<Rectangle> getSmallRectangles();

    //zapytanie do sql o konkretny size i zwracanie tego obiektu ktory spelnia warunek, wazny jest ten : i po nim param
    @Query(nativeQuery = true, value = "SELECT * FROM RECTANGLE rect WHERE (2*rect.height + 2*rect.width) >= :size")
    List<Rectangle> getBiggerSizeRectangles(@Param("size") int size);

    //zapytanie do sql o konkretny size i zwracanie tego obiektu ktory spelnia warunek, wazny jest ten : i po nim param
    @Query(nativeQuery = true, value = "SELECT * FROM RECTANGLE rect WHERE (2*rect.height + 2*rect.width) <= :size")
    List<Rectangle> getSmallerSizeRectangles(@Param("size") int size);
}
