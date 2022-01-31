package com.example.luxuryhotel.contrloller;

import com.example.luxuryhotel.entities.Role;
import com.example.luxuryhotel.entities.User;
import com.example.luxuryhotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private UserRepository userRepository;


    @GetMapping
    public String register (){
        return "register";
    }

    @PostMapping
    public String addUser (User user, Model model){
        User userFromDb = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());
        if (userFromDb != null) {
            model.addAttribute("message", "user with such username or email already exist");
            return "register";
        }
        Set<Role> roles= new HashSet<>();
        roles.add (Role.USER);
        user.setActive(true);
        user.setRoles(roles);
        userRepository.save(user);

        return "redirect:/login";
    }
}
