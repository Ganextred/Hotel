package com.example.luxuryhotel.contrloller;

import com.example.luxuryhotel.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class LanguageController {
    @PostMapping("/setlanguage")
    public ResponseEntity<?> add(@RequestParam(name="language", required=false, defaultValue="ru") String lang,
                      HttpServletResponse response){
        Cookie lng = new Cookie("lang",lang);
        response.addCookie(lng);
        response.setContentType("text/plain");
        return ResponseEntity.ok().body(HttpStatus.OK);

    }
}
