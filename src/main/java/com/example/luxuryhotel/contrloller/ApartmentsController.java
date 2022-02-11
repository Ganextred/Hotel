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
    public String apartments (Model model){
        if (!model.containsAttribute("apartments")) {
            //Iterable<Apartment> apartments = apartmentManager.getDefaultApartments(LocalDate.now().toString(), LocalDate.now().toString());
            apartmentManager.addDefaultModelSortParams(model, LocalDate.now().toString(), LocalDate.now().toString());
            //model.addAttribute("apartments", apartments);
        }
        return "apartments";
    }
    @PostMapping("/apartments/applySort")
    public String applySort (@RequestParam (name = "arrivalDay", required=false) String arrivalDay,
                             @RequestParam (name = "endDay", required=false) String endDay,
                             @RequestParam(name = "sortParam[]", required=false) String[] sortParams,
                             @RequestParam(name = "orderParam[]") Boolean[] orderParams,
                             @RequestParam Map <String,Boolean> status,
                             RedirectAttributes rA){
        //Iterable <Apartment> apartments = apartmentManager.getSortedApartments(arrivalDay, endDay, sortParams, orderParams, status);
        apartmentManager.addModelFlashSortParams(rA,arrivalDay, endDay, sortParams, orderParams, status);
        // rA.addFlashAttribute("apartments", apartments);
        return "redirect:/apartments";
    }

    @GetMapping("apartment/{apartment}")
    public String editUser(Model model, @PathVariable Apartment apartment){
        model.addAttribute("apartment", apartment);
        return "apartment";
    }

    @PostMapping("/apartment/book/{apartment}")
    public String applySort ( @PathVariable Apartment apartment,
                             @RequestParam (name = "arrivalDay", required=true ) String arrivalDay,
                             @RequestParam (name = "endDay", required=true) String endDay,
                             RedirectAttributes rA){
        rA.addFlashAttribute("messages", new ArrayList<String>());
        BookCommand bc = commandFactory.getBookCommand(arrivalDay, endDay, apartment);
        List<String> messages = bc.execute();
        if (messages.size() != 0) {
            rA.addFlashAttribute("messages", messages);
            return ("redirect:/apartment/"+apartment.getId());
        } else{
            return "redirect:/apartments";
        }
    }



}
