package com.example.luxuryhotel.model.repository;

import com.example.luxuryhotel.entities.Request;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface RequestRepository extends CrudRepository<Request, Integer> {
    @Transactional
    Integer deleteRequestByAnswerStatus_PayTimeLimitBeforeOrEndDayBefore(LocalDateTime dateTime, LocalDate localDate);
    List<Request> findByAndAnswerStatusIsNull();
    List<Request> findByAndAnswerStatusIsNotNull();
}
