package pl.bykowski.rectangleapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ExampleController {
    @RequestMapping("/przyklad/model")
    public String przykladModelu(Model model) {
        model.addAttribute("message", "To jest jakaś super informacja"); return "glowny";
    }
}
