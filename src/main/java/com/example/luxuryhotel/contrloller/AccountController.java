package com.example.luxuryhotel.contrloller;

import com.example.luxuryhotel.entities.User;
import com.example.luxuryhotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/account")
    public String account(Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users",users);
        return "account";
    }
}
