package com.plainplanner.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.plainplanner.entities.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.getUserByUsername(username);
		
		if (user == null) {
			throw new UsernameNotFoundException("The username doesn't exist!");
		}

        return org.springframework.security.core.userdetails.User.builder()
        		.username(user.getUsername())
        		.password(user.getPasswordHash())
        		.authorities(user.getRole())
        		.build();
	}
}
