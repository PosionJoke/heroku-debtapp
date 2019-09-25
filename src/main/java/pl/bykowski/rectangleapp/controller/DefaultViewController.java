package pl.bykowski.rectangleapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DefaultViewController {

    @GetMapping("/default-view")
    public ModelAndView returnDefaultView() {
        return new ModelAndView("default-view");
    }

    @GetMapping("/")
    public ModelAndView showMainViewTemporaryMethod() {
        return new ModelAndView("default-view");
    }
}
