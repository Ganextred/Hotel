package com.example.luxuryhotel.repository;

import com.example.luxuryhotel.entities.Request;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RequestRepository extends CrudRepository<Request, Integer> {
    List<Request> findByAndAnswerStatusIsNull();

    List<Request> findByAndAnswerStatusIsNotNull();
}
