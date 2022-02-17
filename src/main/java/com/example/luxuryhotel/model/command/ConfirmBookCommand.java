package com.example.luxuryhotel.model.command;

import com.example.luxuryhotel.entities.ApartmentStatus;
import com.example.luxuryhotel.model.service.ApartmentManager;

import java.util.List;

public class ConfirmBookCommand implements Command{
    ApartmentStatus apartmentStatus;
    ApartmentManager apartmentManager;
    List<String>status;

    public ConfirmBookCommand(ApartmentStatus apartmentStatus,ApartmentManager apartmentManager) {
        this.apartmentStatus = apartmentStatus;
        this.apartmentManager = apartmentManager;
    }

    @Override
    public List<String> execute() {
        List<String>status = apartmentManager.confirmBook(apartmentStatus);
        if (status.size() == 0)
            save();
        return status;
    }
    private void save(){}
}
