package com.example.luxuryhotel.config;

import com.example.luxuryhotel.model.ApartmentManager;
import com.example.luxuryhotel.repository.ApartmentStatusRepository;
import com.example.luxuryhotel.repository.RequestRepository;
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
    @Autowired
    RequestRepository requestRepo;


    private final static Logger logger = Logger.getLogger(ApartmentManager.class);


    @PostConstruct
    private void init() {
        logger.info("Start db initialization");
        requestRepo.deleteRequestByAnswerStatus_PayTimeLimitBeforeOrEndDayBefore(LocalDateTime.now(), LocalDate.now());
        apartmentStatusRepo.deleteApartmentStatusByPayTimeLimitBeforeOrEndDayBefore(LocalDateTime.now(), LocalDate.now());

    }
}
