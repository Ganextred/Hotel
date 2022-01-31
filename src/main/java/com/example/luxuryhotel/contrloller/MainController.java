package com.example.luxuryhotel.contrloller;


import com.example.luxuryhotel.entities.User;
import com.example.luxuryhotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String greeting(Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users",users);
        return "greeting";
    }

    @PostMapping("/adduser")
    public String add(@RequestParam(name="username", required=false, defaultValue="World") String username,
                      @RequestParam(name="email", required=false, defaultValue="Fdsf") String email,
                      Model model){
        User hotelUser = new User(username,email);
        userRepository.save(hotelUser);
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users",users);
        return "account";
    }

}