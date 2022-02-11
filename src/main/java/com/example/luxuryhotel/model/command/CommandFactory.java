package com.example.luxuryhotel.model.command;

import com.example.luxuryhotel.entities.Apartment;
import com.example.luxuryhotel.entities.ApartmentStatus;
import com.example.luxuryhotel.entities.Request;
import com.example.luxuryhotel.entities.User;
import com.example.luxuryhotel.model.ApartmentManager;
import com.example.luxuryhotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CommandFactory {
    @Autowired
    ApartmentManager apartmentManager;
    @Autowired
    UserRepository userRepo;

    public BookCommand getBookCommand (String arrivalDay, String endDay, Apartment apartment){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findByUsername(auth.getName());
        return new BookCommand(arrivalDay, endDay, user, apartment,apartmentManager);
    }
    public ConfirmBookCommand getConfirmBookCommand (ApartmentStatus apartmentStatus){
        return new ConfirmBookCommand(apartmentStatus,apartmentManager);
    }
    public AnswerRequestCommand getAnswerRequestCommand (Request request, Apartment apartment){
        return new AnswerRequestCommand(request, apartment, apartmentManager);
    }

}
