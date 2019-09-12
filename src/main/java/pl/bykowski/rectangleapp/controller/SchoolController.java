package pl.bykowski.rectangleapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.bykowski.rectangleapp.model.tests.School;
import pl.bykowski.rectangleapp.model.tests.SchoolRepository;
import pl.bykowski.rectangleapp.model.tests.Student;
import pl.bykowski.rectangleapp.model.tests.StudentRepository;

@Controller
public class SchoolController {

    private SchoolRepository schoolRepository;
    private StudentRepository studentRepository;

    public SchoolController(SchoolRepository schoolRepository, StudentRepository studentRepository) {
        this.schoolRepository = schoolRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/cns")
    public ModelAndView cns(){
        return new ModelAndView("cns")
                .addObject("school", new School());
    }

    @PostMapping("/cns")
    public ModelAndView cnsPost(@ModelAttribute School school){

        School school1 = new School();
        school1.setName(school.getName());
        school1.setStreet(school.getStreet());
        schoolRepository.save(school1);

        Student student1 = new Student();
        student1.setAge(23);
        student1.setStudentName("Adrian3");
        student1.setOrder(school1);
        studentRepository.save(student1);

        Student student2 = new Student();
        student2.setAge(24);
        student2.setStudentName("Adrian4");
        student2.setOrder(school1);
        studentRepository.save(student2);


        return new ModelAndView("l2p");
    }
}
