package com.eventconnect.repository;

import com.eventconnect.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository // Marks this interface as a Spring Data repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    // Find all bookings for a specific user, ordered by creation date descending
    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    // Find all bookings for a specific event with a given booking status
    List<Booking> findByEventIdAndBookingStatus(Long eventId, Booking.BookingStatus status);
    
    // Count the number of bookings a user has made since a specific date/time
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user.id = :userId AND b.createdAt >= :since")
    long countBookingsByUserSince(@Param("userId") Long userId, @Param("since") LocalDateTime since);
}
