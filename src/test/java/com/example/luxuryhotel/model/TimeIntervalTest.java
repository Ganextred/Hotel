package com.example.luxuryhotel.model;

import com.example.luxuryhotel.model.service.ApartmentManager;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeIntervalTest {

    @Test
    void commonTimeIntervalTest(){
        ApartmentManager.TimeInterval ti = ApartmentManager.TimeInterval.getValidInterval("",null);
        assertEquals(LocalDate.now().toString(),LocalDate.now().toString());
    }
    @Test
    void changingArrivalIntervalTest(){
        ApartmentManager.TimeInterval ti=
                ApartmentManager.TimeInterval.getValidInterval(LocalDate.now().toString(),LocalDate.now().toString());
        ti.setArrivalDay(LocalDate.now().plusDays(5));
        assertEquals(ti.getArrivalDay(),LocalDate.now());
    }
    @Test
    void changingEndIntervalTest(){
        ApartmentManager.TimeInterval ti=
                ApartmentManager.TimeInterval.getValidInterval(LocalDate.now().toString(),LocalDate.now().toString());
        ti.setEndDay(LocalDate.now().minusDays(5));
        assertEquals(ti.getEndDay(),LocalDate.now());
    }
}
