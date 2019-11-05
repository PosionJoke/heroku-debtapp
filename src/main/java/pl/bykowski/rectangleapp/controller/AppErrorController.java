package pl.bykowski.rectangleapp.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Log4j
@Controller
public class AppErrorController implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request) {
        Optional<Object> status = Optional.of(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));

        Integer statusCode = Integer.valueOf(status.get().toString());

        log.debug(String.format("HTTP error [%s]", statusCode));

        return new ModelAndView("error/default-error-page")
                .addObject("statusCode", statusCode);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
