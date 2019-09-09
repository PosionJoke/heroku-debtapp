package pl.bykowski.rectangleapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pl.bykowski.rectangleapp.services.DebtorService;

//@RestController
//@RequestMapping("/campaigns")
@RestController
//@RequestMapping("/hellow")
public class HelloWorldController {

    private DebtorService debtorService;

    public HelloWorldController(DebtorService debtorService) {
        this.debtorService = debtorService;
    }

    @GetMapping("/hellow")
    public ModelAndView hello(){
        return new ModelAndView("hello")
                .addObject(debtorService.findUserName());
    }

    @GetMapping("/hellow2")
    public ModelAndView hello2(){
        return new ModelAndView("hello");
    }

    @GetMapping("/hellow3")
    public ModelAndView hello3(){
        return new ModelAndView("create-new-user");
    }

}
