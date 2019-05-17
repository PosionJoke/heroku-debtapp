package pl.bykowski.rectangleapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//dzieki temu nasza odpowiedź bedzie widoczna w przeglądarce
@RestController
public class FigureApi {

    //metoda do webu?
    @GetMapping("/calculate")
    public int calculate(@RequestParam String figure, @RequestParam int siteA, @RequestParam int siteB, @RequestParam int siteC){
        if(figure.equalsIgnoreCase("RECTANGLE")){
            return 2*siteA + 2*siteB;
        }
        else if(figure.equalsIgnoreCase("TRIANGLE")){
            return siteA + siteB + siteC;
        }
        return -1;
    }
}
