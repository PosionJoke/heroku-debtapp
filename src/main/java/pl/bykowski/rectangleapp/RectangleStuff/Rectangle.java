package pl.bykowski.rectangleapp.RectangleStuff;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//oznaczamy klase jako element bazy danych
@Entity
public class Rectangle {
    //oznaczamy pole jako ID dla bazy danych
    //autoincrement id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int height;
    private int width;


    //nalezy zrobic bezargumentowy konstruktor
    public Rectangle() {
    }

    //konstruktor z wszystkimi argumentami
    public Rectangle(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Long getId() {
        return id;
    }

    public int getHeight() {

        return height;
    }

    public int getWidth() {
        return width;
    }


    @Override
    public String toString() {
        return "Rectangle{" +
                "id=" + id +
                ", height=" + height +
                ", width=" + width +
                '}';
    }
}
