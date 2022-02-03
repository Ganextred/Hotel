package com.example.luxuryhotel.contrloller;

import com.example.luxuryhotel.entities.Role;
import com.example.luxuryhotel.entities.User;
import com.example.luxuryhotel.model.Validator;
import com.example.luxuryhotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    Validator valid;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String register (){
        return "register";
    }

    @PostMapping
    public String addUser (User user, Model model){
        List<String> s=valid.regUser(user);
        if (s.size()!=0) {
            model.addAttribute("message",s);
            return "register";
        }
        Set<Role> roles= new HashSet<>();
        roles.add (Role.USER);
        user.setActive(true);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/login";
    }
//    public String addUser (User user, Model model){
//        //model.addAttribute("rb", ResourceBundle.getBundle("hotel", new Locale("en")));
//        String validStatus = valid.regUser(user);
//        if (!validStatus.equals("valid")) {
//            model.addAttribute("message", validStatus);
//            return "register";
//        }
//        Set<Role> roles= new HashSet<>();
//        roles.add (Role.USER);
//        user.setActive(true);
//        user.setRoles(roles);
//        userRepository.save(user);
//
//        return "redirect:/login";
//    }
}
