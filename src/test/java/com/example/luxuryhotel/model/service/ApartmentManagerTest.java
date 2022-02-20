package com.example.luxuryhotel.model.service;

import com.example.luxuryhotel.entities.Apartment;
import com.example.luxuryhotel.entities.ApartmentStatus;
import com.example.luxuryhotel.model.repository.ApartmentStatusRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ApartmentManagerTest {
    @Autowired
    ApartmentManager apartmentManager;
    @Autowired
    ApartmentStatusRepository apartmentStatusRepo;
    @Test
    void getSort() {
        Sort sort = apartmentManager.getSort(null,null);
        assertFalse(sort.isEmpty());
        Boolean[] op = new Boolean[]{};
        String[] sp = new String[]{};
        sort = apartmentManager.getSort(sp, op);
        assertFalse(sort.isEmpty());
        op = new Boolean[]{true, true};
        sp = new String[]{"price"};
        sort = apartmentManager.getSort(sp,op);
        assertFalse(sort.isEmpty());
    }

    @Test
    void getSortedApartments() {
        Map<String,Boolean> m = new HashMap<>();
        m.put ("BOUGHT", true);
        List<Apartment> apartments = apartmentManager.getSortedApartments(LocalDate.now().toString(),LocalDate.now().toString(),new String[]{"price"}, new Boolean[]{true},m,1);
        for (int i = 1; i< apartments.size(); i++) {
            assertTrue(apartments.get(i).getPrice() >= apartments.get(i - 1).getPrice());
            List<ApartmentStatus> apartmentStatuses = apartmentStatusRepo.findApartmentStatusByApartmentId(apartments.get(i));
            boolean f = false;
            if (apartmentStatuses.size() != 0) {
                for (ApartmentStatus as : apartmentStatuses)
                    if (as.getStatus().toString().equals("BOUGHT"))
                        f = true;
            }
            assertTrue(f);
        }
    }

    @Test
    void getDefaultApartments() {
        List<Apartment> apartments = apartmentManager.getDefaultApartments(LocalDate.now().toString(),LocalDate.now().toString(), 1);
        for (int i = 1; i< apartments.size(); i++)
            assertTrue(apartments.get(i).getPrice()>=apartments.get(i-1).getPrice());
    }
}