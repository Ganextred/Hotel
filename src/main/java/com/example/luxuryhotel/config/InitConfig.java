package com.example.luxuryhotel.config;

import com.example.luxuryhotel.model.ApartmentManager;
import com.example.luxuryhotel.repository.ApartmentStatusRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class InitConfig {
    @Autowired
    ApartmentStatusRepository apartmentStatusRepo;


    private final static Logger logger = Logger.getLogger(ApartmentManager.class);


    @PostConstruct
    private void init() {
        logger.info("Start db initialization");
        apartmentStatusRepo.deleteApartmentStatusByPayTimeLimitBeforeOrEndDayBefore(LocalDateTime.now(), LocalDate.now());
    }
}
