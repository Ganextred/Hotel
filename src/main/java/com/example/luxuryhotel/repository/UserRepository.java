package com.example.luxuryhotel.repository;

import com.example.luxuryhotel.entities.HotelUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<HotelUser, Integer> {

}