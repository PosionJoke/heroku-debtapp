package pl.bykowski.rectangleapp.model.tests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String street;
    @OneToMany(mappedBy = "order")
//    @JoinColumn(name = "my_students")
    private List<Student> studentList = new ArrayList<>();

}
