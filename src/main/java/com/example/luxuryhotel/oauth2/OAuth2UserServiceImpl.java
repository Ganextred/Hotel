package com.example.luxuryhotel.oauth2;

import com.example.luxuryhotel.entities.User;
import com.example.luxuryhotel.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService  {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	UserDetailsService userDetailsService;
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User user =  super.loadUser(userRequest);
		System.out.println("CustomOAuth2UserService invoked");

		UserDetails userDetails = userDetailsService.loadUserByUsername(user.getAttribute("email"));

		// Create a UsernamePasswordAuthenticationToken with the user's information
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());

		// Set the authenticated user in the SecurityContextHolder
		SecurityContextHolder.getContext().setAuthentication(authentication);


		return new OAuth2UserImpl(user);
	}

}
