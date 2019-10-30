package pl.bykowski.rectangleapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestUserController {
    @GetMapping("/testUser")
    public ModelAndView testUser() {
        return new ModelAndView("testUser");
    }
}
