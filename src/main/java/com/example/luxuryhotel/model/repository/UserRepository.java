package com.example.luxuryhotel.model.repository;

import com.example.luxuryhotel.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUsername(String username);

    User findByEmail(String email);
    User findByUsernameOrEmail(String username, String email);
}