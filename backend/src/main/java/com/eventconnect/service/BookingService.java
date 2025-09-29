package com.eventconnect.service;

import com.eventconnect.dto.BookingRequest;
import com.eventconnect.entity.Booking;
import com.eventconnect.entity.Event;
import com.eventconnect.entity.User;
import com.eventconnect.entity.UserBookingAttempt;
import com.eventconnect.repository.BookingRepository;
import com.eventconnect.repository.EventRepository;
import com.eventconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Transactional
    public Booking createBooking(BookingRequest request, String userEmail) throws Exception {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Rate limiting check
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);
        long recentBookings = bookingRepository.countBookingsByUserSince(user.getId(), oneMinuteAgo);
        
        if (recentBookings >= 5) {
            throw new RuntimeException("Rate limit exceeded. Maximum 5 bookings per minute.");
        }
        
        Event event = eventRepository.findById(request.getEventId())
            .orElseThrow(() -> new RuntimeException("Event not found"));
        
        // Validate seat availability
        if (event.getAvailableSeats() < request.getSeatsBooked()) {
            throw new RuntimeException("Not enough seats available");
        }
        
        // Validate total amount
     // Validate total amount
        BigDecimal expectedAmount = event.getPrice()
                .multiply(BigDecimal.valueOf(request.getSeatsBooked()));

        if (request.getTotalAmount().compareTo(expectedAmount) != 0) {
            throw new RuntimeException("Invalid total amount");
        }

        
        // Generate unique booking reference
        String bookingReference = "EC" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        // Create booking
        Booking booking = new Booking(
            user,
            event,
            request.getSeatsBooked(),
            request.getTotalAmount(),
            bookingReference
        );
        
        // Update available seats
        event.setAvailableSeats(event.getAvailableSeats() - request.getSeatsBooked());
        
        eventRepository.save(event);
        
        return bookingRepository.save(booking);
    }
}
