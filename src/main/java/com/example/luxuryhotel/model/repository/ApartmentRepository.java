package com.example.luxuryhotel.model.repository;

import com.example.luxuryhotel.entities.Apartment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;

@Repository
public interface ApartmentRepository extends CrudRepository<Apartment, Integer> {
    List<Apartment> findAll(Sort sort);


    @Query("SELECT DISTINCT a FROM Apartment a LEFT JOIN ApartmentStatus as2 " +
            "ON a = as2.apartmentId " +
            "AND (as2.endDay >= ?1 AND as2.arrivalDay <= ?2) AND (as2.payTimeLimit IS NULL OR as2.payTimeLimit>=NOW())" +
            "WHERE (as2.status IS NULL AND ?3 = true) OR (as2.status LIKE 'BOOKED%' AND ?4 = true) OR " +
            "(as2.status = 'BOUGHT' AND ?5 = true) OR (as2.status = 'INACCESSIBLE' AND ?6 = true)")
    List<Apartment> findWthStatus (LocalDate arrivalDay, LocalDate endDay,
                                            Boolean needAvailable, Boolean needBooked,
                                            Boolean needBOUGHT, Boolean needInaccessible,
                                            Pageable pageable);
}
