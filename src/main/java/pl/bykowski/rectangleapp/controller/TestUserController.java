package pl.bykowski.rectangleapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class TestUserController {
    @GetMapping("/testUser")
    public ModelAndView testUser(){
        return new ModelAndView("testUser");
    }
}
