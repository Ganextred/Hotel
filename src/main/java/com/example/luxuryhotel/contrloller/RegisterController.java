package com.example.luxuryhotel.contrloller;

import com.example.luxuryhotel.entities.Role;
import com.example.luxuryhotel.entities.User;
import com.example.luxuryhotel.model.service.Validator;
import com.example.luxuryhotel.model.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    Validator valid;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final static Logger logger = Logger.getLogger(RegisterController.class);

    @GetMapping
    public String register (Model model){
        return "register";
    }

    @PostMapping
    public String addUser (User user, Model model, RedirectAttributes rA){
        List<String> s=valid.regUser(user);
        if (s.size()!=0) {
            rA.addFlashAttribute("message",s);
            return ("redirect:/register");
        }

        Set<Role> roles= new HashSet<>();
        roles.add(Role.USER);
        user.setRoles(roles).setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userRepository.save(user);
            logger.info("new user was registered, username: " + user.getUsername());
        } catch (Exception e){
            s.add("User_already_exist");
            rA.addFlashAttribute("message",s);
            return "redirect:/register";
        }

        return ("redirect:/login");
    }

}
