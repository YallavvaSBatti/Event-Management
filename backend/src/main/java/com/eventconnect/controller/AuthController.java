//package com.eventconnect.controller;
//
//import com.eventconnect.dto.LoginRequest;
//import com.eventconnect.dto.RegisterRequest;
//import com.eventconnect.dto.AuthResponse;
//import com.eventconnect.entity.User;
//import com.eventconnect.repository.UserRepository;
//import com.eventconnect.security.JwtUtil;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/auth")
//@CrossOrigin(origins = "http://localhost:3000")
//public class AuthController {
//    
//    @Autowired
//    private UserRepository userRepository;
//    
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    
//    @Autowired
//    private JwtUtil jwtUtil;
//    
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
//        try {
//            if (userRepository.existsByEmail(request.getEmail())) {
//                return ResponseEntity.badRequest().body("Email already exists");
//            }
//            
//            User user = new User(
//                request.getEmail(),
//                passwordEncoder.encode(request.getPassword()),
//                request.getFirstName(),
//                request.getLastName()
//            );
//            user.setPhone(request.getPhone());
//            
//            User savedUser = userRepository.save(user);
//            String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getId());
//            
//            return ResponseEntity.ok(new AuthResponse(token, savedUser.getEmail(), 
//                savedUser.getFirstName() + " " + savedUser.getLastName()));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
//        }
//    }
//    
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
//        try {
//            User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//            
//            if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
//                return ResponseEntity.badRequest().body("Invalid credentials");
//            }
//            
//            String token = jwtUtil.generateToken(user.getEmail(), user.getId());
//            
//            return ResponseEntity.ok(new AuthResponse(token, user.getEmail(), 
//                user.getFirstName() + " " + user.getLastName()));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Login failed: " + e.getMessage());
//        }
//    }
//}

package com.eventconnect.controller;

import com.eventconnect.dto.LoginRequest;
import com.eventconnect.dto.RegisterRequest;
import com.eventconnect.dto.AuthResponse;
import com.eventconnect.entity.User;
import com.eventconnect.repository.UserRepository;
import com.eventconnect.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController  // Marks this class as a REST controller
@RequestMapping("/api/auth") // Base URL for all endpoints in this controller
@CrossOrigin(origins = "http://localhost:3000") // Enable CORS for frontend at localhost:3000
public class AuthController {
    
    @Autowired
    private UserRepository userRepository; // Repository to access User data
    
    @Autowired
    private PasswordEncoder passwordEncoder; // For hashing and verifying passwords
    
    @Autowired
    private JwtUtil jwtUtil; // Utility class to generate JWT tokens
    
    // Endpoint to register a new user
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            // Check if email already exists
            if (userRepository.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest().body("Email already exists");
            }
            
            // Create new User entity and encode the password
            User user = new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getFirstName(),
                request.getLastName()
            );
            user.setPhone(request.getPhone());
            
            // Save the user in the database
            User savedUser = userRepository.save(user);
            
            // Generate JWT token for the registered user
            String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getId());
            
            // Return token and user info as response
            return ResponseEntity.ok(new AuthResponse(token, savedUser.getEmail(), 
                savedUser.getFirstName() + " " + savedUser.getLastName()));
        } catch (Exception e) {
            // Return error message if registration fails
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }
    
    // Endpoint to login a user
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            // Find user by email
            User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Check if the password matches
            if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
                return ResponseEntity.badRequest().body("Invalid credentials");
            }
            
            // Generate JWT token for the logged-in user
            String token = jwtUtil.generateToken(user.getEmail(), user.getId());
            
            // Return token and user info as response
            return ResponseEntity.ok(new AuthResponse(token, user.getEmail(), 
                user.getFirstName() + " " + user.getLastName()));
        } catch (Exception e) {
            // Return error message if login fails
            return ResponseEntity.badRequest().body("Login failed: " + e.getMessage());
        }
    }
}

