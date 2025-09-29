//package com.eventconnect.controller;
//
//import com.eventconnect.dto.BookingRequest;
//import com.eventconnect.entity.Booking;
//import com.eventconnect.entity.User;
//import com.eventconnect.repository.BookingRepository;
//import com.eventconnect.repository.UserRepository;
//import com.eventconnect.service.BookingService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/bookings")
//@CrossOrigin(origins = "http://localhost:3000")
//public class BookingController {
//    
//    @Autowired
//    private BookingService bookingService;
//    
//    @Autowired
//    private BookingRepository bookingRepository;
//    
//    @Autowired
//    private UserRepository userRepository;
//    
//    @PostMapping
//    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequest request, 
//                                         Authentication authentication) {
//        try {
//            String email = authentication.getName();
//            Booking booking = bookingService.createBooking(request, email);
//            return ResponseEntity.ok(booking);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//    
//    @GetMapping("/user")
//    public ResponseEntity<List<Booking>> getUserBookings(Authentication authentication) {
//        try {
//            String email = authentication.getName();
//            User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//            
//            List<Booking> bookings = bookingRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
//            return ResponseEntity.ok(bookings);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//    
//    @GetMapping("/{id}")
//    public ResponseEntity<Booking> getBookingById(@PathVariable Long id, Authentication authentication) {
//        try {
//            String email = authentication.getName();
//            User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//            
//            Booking booking = bookingRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Booking not found"));
//            
//            // Ensure user can only access their own bookings
//            if (!booking.getUser().getId().equals(user.getId())) {
//                //return ResponseEntity.badRequest();
//            }
//            
//            return ResponseEntity.ok(booking);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//}


package com.eventconnect.controller;

import com.eventconnect.dto.BookingRequest;
import com.eventconnect.entity.Booking;
import com.eventconnect.entity.User;
import com.eventconnect.repository.BookingRepository;
import com.eventconnect.repository.UserRepository;
import com.eventconnect.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController  // Marks this class as a REST controller
@RequestMapping("/api/bookings") // Base URL for all booking-related endpoints
@CrossOrigin(origins = "http://localhost:3000") // Allow requests from frontend
public class BookingController {
    
    @Autowired
    private BookingService bookingService; // Service layer for booking logic
    
    @Autowired
    private BookingRepository bookingRepository; // Repository to access Booking data
    
    @Autowired
    private UserRepository userRepository; // Repository to access User data
    
    // Endpoint to create a new booking
    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequest request, 
                                         Authentication authentication) {
        try {
            String email = authentication.getName(); // Get email of currently logged-in user
            Booking booking = bookingService.createBooking(request, email); // Create booking
            return ResponseEntity.ok(booking); // Return created booking
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Return error message
        }
    }
    
    // Endpoint to get all bookings for the logged-in user
    @GetMapping("/user")
    public ResponseEntity<List<Booking>> getUserBookings(Authentication authentication) {
        try {
            String email = authentication.getName(); // Get email of logged-in user
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")); // Find user
            
            List<Booking> bookings = bookingRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
            // Fetch user's bookings, sorted by creation date descending
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Return 400 if error occurs
        }
    }
    
    // Endpoint to get a specific booking by ID
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id, Authentication authentication) {
        try {
            String email = authentication.getName(); // Get email of logged-in user
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")); // Find user
            
            Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found")); // Find booking
            
            // Ensure user can only access their own bookings
            if (!booking.getUser().getId().equals(user.getId())) {
                // You may return 403 Forbidden here if needed
            }
            
            return ResponseEntity.ok(booking); // Return the booking
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Return 400 if error occurs
        }
    }
}

