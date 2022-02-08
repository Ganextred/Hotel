package com.example.luxuryhotel.contrloller;


import com.example.luxuryhotel.entities.ApartmentStatus;
import com.example.luxuryhotel.entities.Role;
import com.example.luxuryhotel.entities.Status;
import com.example.luxuryhotel.entities.User;
import com.example.luxuryhotel.model.ApartmentManager;
import com.example.luxuryhotel.model.command.CommandFactory;
import com.example.luxuryhotel.model.command.ConfirmBookCommand;
import com.example.luxuryhotel.repository.ApartmentStatusRepository;
import com.example.luxuryhotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserRepository userRepo;
    @Autowired
    ApartmentStatusRepository apartmentStatusRepo;
    @Autowired
    CommandFactory commandFactory;

    @GetMapping("/adminPanel")
    public String adminPanel(Model model){
        model.addAttribute("users", userRepo.findAll());
        List<ApartmentStatus> bStatuses = apartmentStatusRepo.findApartmentStatusByStatusAndPayTimeLimitAfter(Status.BOOKED, LocalDateTime.now());

        model.addAttribute("bStatuses", bStatuses);
        return "adminPanel";
    }
    @GetMapping("editUser/{user}")
    public String editUser(Model model, @PathVariable User user){
        model.addAttribute("user", user);
        model.addAttribute("existingRoles",Role.values());
        return "editUser";
    }
    @PostMapping("confirmStatus")
    public String confirmBook(@RequestParam(name = "status") ApartmentStatus apartmentStatus){
        ConfirmBookCommand command= commandFactory.getConfirmBookCommand(apartmentStatus);
        command.execute();
        return"redirect:/admin/adminPanel";
    }
}