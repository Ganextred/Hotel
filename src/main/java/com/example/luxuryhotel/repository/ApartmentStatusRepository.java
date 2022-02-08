package com.example.luxuryhotel.repository;

import com.example.luxuryhotel.entities.ApartmentStatus;
import com.example.luxuryhotel.entities.Status;
import com.example.luxuryhotel.entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApartmentStatusRepository extends CrudRepository<ApartmentStatus, Integer> {
    @Transactional
    List<ApartmentStatus> deleteApartmentStatusByPayTimeLimitBeforeOrEndDayBefore(LocalDateTime dateTime, LocalDate localDate);

    List<ApartmentStatus> findApartmentStatusByStatusAndPayTimeLimitAfter(Status status, LocalDateTime localDateTime);

}
