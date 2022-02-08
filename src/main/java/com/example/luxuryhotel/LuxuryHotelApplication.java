package com.example.luxuryhotel;

import com.example.luxuryhotel.repository.ApartmentStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@SpringBootApplication
public class LuxuryHotelApplication {
    public static void main(String[] args) {
        SpringApplication.run(LuxuryHotelApplication.class, args);
    }
}
