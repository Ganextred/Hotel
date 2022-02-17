package com.example.luxuryhotel.contrloller;

import com.example.luxuryhotel.model.service.CookieManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LanguageController {
    @Autowired
    CookieManager cookieManager;
    @GetMapping("/lang")
    public ResponseEntity<?> lang(HttpServletRequest request, HttpServletResponse response){
        cookieManager.changLang();
        return ResponseEntity.ok().body(HttpStatus.OK);
    }
}
