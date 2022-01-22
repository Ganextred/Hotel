package com.example.luxuryhotel;


import com.example.luxuryhotel.entities.HotelUser;
import com.example.luxuryhotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/greeting")
    public String greeting(Model model) {
        Iterable<HotelUser> users = userRepository.findAll();
        model.addAttribute("users",users);
        return "greeting";
    }
    @PostMapping("/greeting")
    public String add(@RequestParam(name="username", required=false, defaultValue="World") String username,
                      @RequestParam(name="email", required=false, defaultValue="Fdsf") String email,
                      Model model){
        HotelUser hotelUser = new HotelUser(username,email);
        userRepository.save(hotelUser);
        Iterable<HotelUser> users = userRepository.findAll();
        model.addAttribute("users",users);
        return "greeting";
    }

}