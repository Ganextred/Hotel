package com.example.luxuryhotel.oauth2;

import com.example.luxuryhotel.entities.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class OAuth2UserImpl implements OAuth2User {

	private final OAuth2User oauth2User;

	public OAuth2UserImpl(OAuth2User oauth2User) {
		this.oauth2User = oauth2User;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return oauth2User.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(Role.USER);//oauth2User.getAuthorities();
	}

	@Override
	public String getName() {	
		return oauth2User.getAttribute("name");
	}

	public String getEmail() {
		return oauth2User.getAttribute("email");
	}
}
