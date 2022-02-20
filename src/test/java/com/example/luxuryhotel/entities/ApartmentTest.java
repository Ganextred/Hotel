package com.example.luxuryhotel.entities;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApartmentTest {

    @Test
    void getImage() {
        Apartment a = new Apartment();
        List<String> inputImages = new ArrayList<>();
        inputImages.add("randomSymbolsnfdsjgsfhgjs");
        a.setImages(inputImages);
        assertEquals(a.getImage(),"defaultRoom.jpg");
    }
}