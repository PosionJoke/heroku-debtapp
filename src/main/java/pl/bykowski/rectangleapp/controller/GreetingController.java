package pl.bykowski.rectangleapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bykowski.rectangleapp.services.GreetingService;

@RestController
public class GreetingController {


    private final GreetingService service;

    public GreetingController(GreetingService service) {
        this.service = service;
    }
    @GetMapping("/greeting")
    public String greeting() {
        return service.greet();
    }

    @GetMapping("/private/hello")
    public String privateHello(){
        return service.greet();
    }
}