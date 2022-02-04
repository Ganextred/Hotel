package com.example.luxuryhotel.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Service
@RequestScope
public class CookieManager {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    LocaleResolver localeResolver;

    public String findCookiesByName(String name){
        String cook=null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(name))
                cook = cookie.getValue();
        }
        return cook;
    };

    public void changLang(){
        String lang = findCookiesByName("lang");
        if (lang == null)
            lang = "ru";
        else if (lang.equals("en"))
            lang = "ru";
        else if (lang.equals("ru"))
            lang = "en";
        localeResolver.setLocale(request,response, new Locale(lang));
        System.out.println(lang);

    }
}