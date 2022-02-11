package com.example.luxuryhotel.model.command;

import com.example.luxuryhotel.entities.Apartment;
import com.example.luxuryhotel.entities.Request;
import com.example.luxuryhotel.entities.User;
import com.example.luxuryhotel.model.ApartmentManager;

import java.util.List;

public class AnswerRequestCommand implements Command {
    Request request;
    Apartment apartment;
    List<String> status;
    ApartmentManager apartmentManager;

    public AnswerRequestCommand(Request request, Apartment apartment, ApartmentManager apartmentManager) {
        this.request = request;
        this.apartment = apartment;
        this.apartmentManager = apartmentManager;
    }

    @Override
    public List<String> execute() {
        status = apartmentManager.answerRequest(request, apartment);
        if (status.size() == 0)
            save();
        return status;
    }
    private void save(){}
}
