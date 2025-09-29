package com.eventconnect.security;

import com.eventconnect.entity.User;
import com.eventconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service // Marks this class as a Spring service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository; // Repository to access User data
    
    // Method called by Spring Security to load a user by username (email in our case)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Fetch user from database by email
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        // Return Spring Security User object with email, password, active status, and roles/authorities
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),           // username
            user.getPasswordHash(),    // password
            user.getIsActive(),        // enabled (active) status
            true,                      // accountNonExpired
            true,                      // credentialsNonExpired
            true,                      // accountNonLocked
            new ArrayList<>()          // authorities/roles (empty in this case)
        );
    }
}
