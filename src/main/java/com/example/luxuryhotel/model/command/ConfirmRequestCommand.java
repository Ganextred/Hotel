package com.example.luxuryhotel.model.command;

import com.example.luxuryhotel.entities.Request;
import com.example.luxuryhotel.model.service.ApartmentManager;

import java.util.List;

public class ConfirmRequestCommand implements Command {
    Request request;
    List<String> status;
    ApartmentManager apartmentManager;

    public ConfirmRequestCommand(Request request, ApartmentManager apartmentManager) {
        this.request = request;
        this.apartmentManager = apartmentManager;
    }

    @Override
    public List<String> execute() {
        status = apartmentManager.confirmRequest(request);
        if (status.size() == 0)
            save();
        return status;
    }
    private void save(){}
}
