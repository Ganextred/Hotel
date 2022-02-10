package com.example.luxuryhotel.model;

import com.example.luxuryhotel.entities.Clazz;
import com.example.luxuryhotel.entities.Request;
import com.example.luxuryhotel.entities.User;
import com.example.luxuryhotel.repository.RequestRepository;
import com.example.luxuryhotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserManager {
    @Autowired
    Validator valid;
    @Autowired
    UserRepository userRepo;
    @Autowired
    RequestRepository requestRepo;

    @Transactional
    public List<String> sendRequest(String arrivalDay, String endDay, Clazz clazz, Integer beds,String wishes){
        List<String> messages = valid.sendRequest(arrivalDay,endDay, clazz, beds, wishes);
        if (messages.size() == 0){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepo.findByUsername(auth.getName());
            Request r = new Request(user,beds,LocalDate.parse(arrivalDay), LocalDate.parse(endDay), clazz, wishes, false);
            requestRepo.save(r);
        }
        return messages;
    }
}
