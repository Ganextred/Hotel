package com.example.luxuryhotel.contrloller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

public class ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("massage", "Page_not_found");
                model.addAttribute("error", "404");
                return "error";
            }
        }
        model.addAttribute("massage", "Something_went_wrong");
        model.addAttribute("error", "Error");
        return "error";
    }

    @GetMapping("/error403")
    public String handleError403() {
        return "error403";
    }
}
