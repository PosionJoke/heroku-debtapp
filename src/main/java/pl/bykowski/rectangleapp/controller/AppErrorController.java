package pl.bykowski.rectangleapp.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.LastModified;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Log4j
@Controller
public class AppErrorController implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request){
//TODO This have no sens, look! what happens when we got 403 error? yep! We dont have any 404 error.html!
//TODO in this case we should make list? or switch or w/e just some thing what can search in available html error page
//TODO if not just return default-error-page!
        Optional<Object> status = Optional.of(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));

        if(status.isPresent()){
            Integer statusCode = Integer.valueOf(status.toString());

            return showErrorHtml(statusCode);
        }else {
            return new ModelAndView("error/default-error-page");
        }
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    private ModelAndView showErrorHtml(Integer errorCode){
        return new ModelAndView("error/error-" + errorCode)
                .addObject("errorCode", errorCode);
    }
}
