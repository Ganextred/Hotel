package com.example.luxuryhotel.model.command;

import com.example.luxuryhotel.entities.Apartment;
import com.example.luxuryhotel.entities.User;
import com.example.luxuryhotel.model.ApartmentManager;


import java.util.List;


public class BookCommand implements  Command{


    String arrivalDay; String endDay;
    User user;
    Apartment apartment;
    List<String>status;
    ApartmentManager apartmentManager;

    public BookCommand(String arrivalDay, String endDay, User user, Apartment apartment, ApartmentManager apartmentManager) {
        this.arrivalDay = arrivalDay;
        this.endDay = endDay;
        this.user = user;
        this.apartment = apartment;
        this.apartmentManager = apartmentManager;
    }

    @Override
    public List<String> execute() {
        status = apartmentManager.book(arrivalDay,endDay,user,apartment);
        if (status.size() == 0)
            save();
        return status;
    }
    private void save(){}
}
