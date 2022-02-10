package com.example.luxuryhotel.contrloller;

import com.example.luxuryhotel.entities.*;
import com.example.luxuryhotel.model.UserManager;
import com.example.luxuryhotel.model.command.BookCommand;
import com.example.luxuryhotel.model.command.CommandFactory;
import com.example.luxuryhotel.repository.ApartmentStatusRepository;
import com.example.luxuryhotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AccountController {
    @Autowired
    UserRepository userRepo;
    @Autowired
    ApartmentStatusRepository apartmentStatusRepo;
    @Autowired
    UserManager userManager;


    @GetMapping("/account")
    public String account(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findByUsername(auth.getName());
        List<ApartmentStatus> bStatuses =
                apartmentStatusRepo.findApartmentStatusByUserAndStatusAndPayTimeLimitAfter(user,Status.BOOKED,LocalDateTime.now());
        List<ApartmentStatus> bgStatuses =
                apartmentStatusRepo.findApartmentStatusByUserAndStatus(user,Status.BOUGHT);
        model.addAttribute("user", user);
        model.addAttribute("bStatuses", bStatuses);
        model.addAttribute("bgStatuses", bgStatuses);
        return "account";
    }
    @GetMapping("/requestForm")
    public String requestForm(Model model) {
        return "request";
    }

    @PostMapping("/sendRequest")
    public String sendReuest ( Model model,
                              @RequestParam(name = "arrivalDay", required=true ) String arrivalDay,
                              @RequestParam (name = "endDay", required=true) String endDay,
                              @RequestParam (name = "clazz", required=true) Clazz clazz,
                              @RequestParam (name = "beds", required=true) Integer beds,
                               @RequestParam (name = "wishes", required=true) String wishes,
                              RedirectAttributes rA){
        rA.addFlashAttribute("messages", new ArrayList<String>());
        List<String> messages = userManager.sendRequest(arrivalDay,endDay,clazz,beds,wishes);
        if (messages.size() != 0) {
            rA.addFlashAttribute("messages", messages);
            return ("redirect:/requestForm");
        } else{
            return "redirect:/account";
        }
    }
}
