package com.example.luxuryhotel.model.repository;

import com.example.luxuryhotel.entities.Apartment;
import com.example.luxuryhotel.model.service.ApartmentManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ApartmentRepositoryTest {
    @Autowired
    ApartmentRepository apartmentRepo;
    @Autowired
    ApartmentManager apartmentManager;
    @Test
    void findWthStatus() {
        Map<String,Boolean> m = new HashMap<>();
        final int size = 10;
        Pageable pageable = PageRequest.of(0,size,apartmentManager.getSort(new String[]{"price"}, new Boolean[]{true}));
        List<Apartment> apartments = apartmentRepo.findWthStatus(
                LocalDate.now(),LocalDate.now(),true,true,true,true, pageable);
        assertTrue(apartments.size()<=size);
        for (int i = 1; i< apartments.size(); i++)
            assertTrue(apartments.get(i).getPrice()>=apartments.get(i-1).getPrice());
    }
}