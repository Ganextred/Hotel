package com.example.luxuryhotel.contrloller;

import com.example.luxuryhotel.entities.Role;
import com.example.luxuryhotel.entities.User;
import com.example.luxuryhotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
public class RegisterController {
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/register")
    public String register (){
        return "register";
    }

    @PostMapping("/register")
    public String addUser (User user, Model model){
        User userFromDb = userRepository.findByUsername(user.getUsername());
        if (userFromDb != null) {
            model.addAttribute("message", "user already exist");
            return "register";
        }
        Set<Role> roles= new HashSet<>();
        roles.add (Role.USER);
        user.setActive(true);
        user.setRoles(roles);
        user.setEmail("null@gmail.com");
        userRepository.save(user);

        return "redirect:/login";
    }
}
