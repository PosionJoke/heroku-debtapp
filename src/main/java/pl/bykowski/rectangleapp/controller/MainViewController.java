package pl.bykowski.rectangleapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@RestController
public class MainViewController {

    @GetMapping("/main-view")
    public ModelAndView mainView(Principal principal){
        return new ModelAndView("main-view")
                .addObject("user", principal);
    }

}
