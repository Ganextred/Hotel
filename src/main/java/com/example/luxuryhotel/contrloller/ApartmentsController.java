package com.example.luxuryhotel.contrloller;

import com.example.luxuryhotel.entities.Apartment;
import com.example.luxuryhotel.entities.User;
import com.example.luxuryhotel.model.ApartmentManager;
import com.example.luxuryhotel.model.Validator;
import com.example.luxuryhotel.model.command.BookCommand;
import com.example.luxuryhotel.model.command.CommandFactory;
import com.example.luxuryhotel.repository.ApartmentRepository;
import com.example.luxuryhotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
public class ApartmentsController {
    @Autowired
    ApartmentRepository apartRepo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    ApartmentManager apartmentManager;
    @Autowired
    CommandFactory commandFactory;
    @Autowired
    Validator valid;



    @GetMapping("/apartments")
    public String apartments (Model model, @RequestParam(name = "page", required = false, defaultValue = "1") Integer page){
        if (page <1)
            page =1;
        if (!model.containsAttribute("apartments")) {
            apartmentManager.addDefaultModelSortParams(model, LocalDate.now().toString(), LocalDate.now().toString(), page);
        }
        return "apartments";
    }
    @PostMapping("/apartments/applySort")
    public String applySort (@RequestParam (name = "page", required = false, defaultValue = "1") Integer page,
                             @RequestParam (name = "arrivalDay") String arrivalDay,
                             @RequestParam (name = "endDay") String endDay,
                             @RequestParam(name = "sortParam[]") String[] sortParams,
                             @RequestParam(name = "orderParam[]") Boolean[] orderParams,
                             @RequestParam Map <String,Boolean> status,
                             RedirectAttributes rA){
        if (page <1)
            page =1;
        apartmentManager.addModelFlashSortParams(rA,arrivalDay, endDay, sortParams, orderParams, status, page);
        return "redirect:/apartments?page="+page.toString();
    }

    @GetMapping("apartment/")
    public String apartment(Model model, @RequestParam Apartment apartment){
        model.addAttribute("apartment", apartment);
        return "apartment";
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/apartment/book/")
    public String applySort ( @RequestParam Apartment apartment,
                             @RequestParam (name = "arrivalDay", required=true ) String arrivalDay,
                             @RequestParam (name = "endDay", required=true) String endDay,
                             RedirectAttributes rA){
        rA.addFlashAttribute("messages", new ArrayList<String>());
        BookCommand bc = commandFactory.getBookCommand(arrivalDay, endDay, apartment);
        List<String> messages = bc.execute();
        if (messages.size() != 0) {
            rA.addFlashAttribute("messages", messages);
            return ("redirect:/apartment/?apartment="+apartment.getId());
        } else{
            return "redirect:/apartments";
        }
    }



}
