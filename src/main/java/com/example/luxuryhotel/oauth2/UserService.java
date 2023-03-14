package com.example.luxuryhotel.oauth2;

import com.example.luxuryhotel.entities.User;
import com.example.luxuryhotel.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);

        System.out.println("UserService invoked");


        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return new UserDetailsImpl(user);
    }
}
