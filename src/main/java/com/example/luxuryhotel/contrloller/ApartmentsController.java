package com.example.luxuryhotel.contrloller;

import com.example.luxuryhotel.entities.Apartment;
import com.example.luxuryhotel.model.ApartmentManager;
import com.example.luxuryhotel.repository.ApartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.http.HttpRequest;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/apartments")
public class ApartmentsController {
    @Autowired
    ApartmentRepository apartRepo;
    @Autowired
    ApartmentManager apartmentManager;

    @GetMapping
    public String apartments (Model model){
        if (!model.containsAttribute("apartments")) {
            Iterable<Apartment> apartments = apartmentManager.getDefaultApartments();
            model.addAttribute("apartments", apartments);
        }
        return "apartments";
    }
    @PostMapping("/applySort")
    public String applySort (@RequestParam (name = "arrivalDay") String arrivalDay,
                             @RequestParam (name = "endDay") String endDay,
                             @RequestParam(name = "sortParam[]", required=false) String[] sortParams,
                             @RequestParam(name = "orderParam[]") Boolean[] orderParams,
                             @RequestParam Map <String,Boolean> status,
                             RedirectAttributes rA){
        Iterable <Apartment> apartments = apartmentManager.getSortedApartments(arrivalDay, endDay, sortParams, orderParams, status);
        rA.addFlashAttribute("apartments", apartments);
        return "redirect:/apartments";
    }

}
