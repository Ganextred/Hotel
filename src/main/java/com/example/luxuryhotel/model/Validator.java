package com.example.luxuryhotel.model;

import com.example.luxuryhotel.entities.User;
import com.example.luxuryhotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class Validator {
    @Autowired
    private UserRepository userRepository;

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    private static final Pattern VALID_USERNAME_REGEX =
            Pattern.compile("^[a-zA-Z0-9_]{4,30}$");
    private static final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("(?=^.{6,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$");

    public static boolean email(String email){return (email!=null && email.matches(VALID_EMAIL_ADDRESS_REGEX.pattern()));}
    public static boolean userName(String un){return (un!=null && un.matches(VALID_USERNAME_REGEX.pattern()));}
    public static boolean password(String pass){return (pass!=null && pass.matches(VALID_PASSWORD_REGEX.pattern()));}

    public List<String> regUser(User user){
        List<String> s=new ArrayList<>();
        if (!userName(user.getUsername())) {
            s.add("incorrectUsername");
        }
        if (!email(user.getEmail())) {
            s.add("incorrectEmail");
        }
        if (!password(user.getPassword())){
            s.add("incorrectPassword");
        }
        User userFromDb = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());
        if (userFromDb != null) {
            s.add("userAlreadyExist");
        }
        return s;
    }
}
//    public String regUser (User user){
//        if (!userName (user.getUsername())){
//            return "incorrectUsername";
//        }
//        if (!email (user.getEmail())){
//            return "incorrectEmail";
//        }
//        if (!password (user.getPassword())){
//            return "incorrectPassword";
//        }
//        User userFromDb = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());
//        if (userFromDb != null) {
//            return "userAlreadyExist";
//        }
//        return "valid";
//    }
