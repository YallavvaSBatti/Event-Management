//package com.eventconnect.security;
//
//import com.eventconnect.repository.UserRepository;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import java.io.IOException;
//
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//    
//    @Autowired
//    private JwtUtil jwtUtil;
//    
//    @Autowired
//    private UserDetailsService userDetailsService;
//    
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
//                                  FilterChain filterChain) throws ServletException, IOException {
//        
//        final String authorizationHeader = request.getHeader("Authorization");
//        
//        String email = null;
//        String jwt = null;
//        
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            jwt = authorizationHeader.substring(7);
//            try {
//                email = jwtUtil.extractEmail(jwt);
//            } catch (Exception e) {
//                logger.error("JWT token extraction failed", e);
//            }
//        }
//        
//        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
//            
//            if (jwtUtil.validateToken(jwt, email)) {
//                UsernamePasswordAuthenticationToken authToken = 
//                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
//        
//        filterChain.doFilter(request, response);
//    }
//}

package com.eventconnect.security;

import com.eventconnect.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component // Marks this as a Spring-managed component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtUtil jwtUtil; // Utility class to handle JWT operations
    
    @Autowired
    private UserDetailsService userDetailsService; // Service to load user details
    
    // Filter method called for every request
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        final String authorizationHeader = request.getHeader("Authorization"); // Get JWT from header
        
        String email = null;
        String jwt = null;
        
        // Check if header contains Bearer token
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Extract token after "Bearer "
            try {
                email = jwtUtil.extractEmail(jwt); // Extract email from JWT
            } catch (Exception e) {
                logger.error("JWT token extraction failed", e); // Log error if extraction fails
            }
        }
        
        // If email extracted and user not already authenticated
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email); // Load user details
            
            // Validate the token
            if (jwtUtil.validateToken(jwt, email)) {
                // Create authentication token and set in security context
                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken); // Set user as authenticated
            }
        }
        
        filterChain.doFilter(request, response); // Continue the filter chain
    }
}
