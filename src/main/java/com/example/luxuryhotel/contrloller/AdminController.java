package com.example.luxuryhotel.contrloller;


import com.example.luxuryhotel.entities.Role;
import com.example.luxuryhotel.entities.User;
import com.example.luxuryhotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserRepository userRepo;

    @GetMapping("/adminPanel")
    public String adminPanel(Model model){
        model.addAttribute("users", userRepo.findAll());
        return "adminPanel";
    }
    @GetMapping("editUser/{user}")
    public String editUser(Model model, @PathVariable User user){
        model.addAttribute("user", user);
        model.addAttribute("existingRoles",Role.values());

        return "editUser";
    }
}