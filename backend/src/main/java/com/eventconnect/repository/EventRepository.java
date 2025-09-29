//package com.eventconnect.repository;
//
//import com.eventconnect.entity.Event;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Repository
//public interface EventRepository extends JpaRepository<Event, Long> {
//    List<Event> findByIsActiveTrueOrderByEventDateAsc();
//    List<Event> findByCategoryAndIsActiveTrueOrderByEventDateAsc(String category);
//    List<Event> findByEventDateAfterAndIsActiveTrueOrderByEventDateAsc(LocalDateTime date);
//    
//    @Query("SELECT e FROM Event e LEFT JOIN e.bookings b WHERE b.bookingStatus = 'CONFIRMED' " +
//           "GROUP BY e.id ORDER BY SUM(b.seatsBooked) DESC, COUNT(b.id) DESC")
//    List<Event> findTop3MostBookedEvents();
//}
//package com.eventconnect.repository;
//
//import com.eventconnect.entity.Event;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Repository
//public interface EventRepository extends JpaRepository<Event, Long> {
//    List<Event> findByIsActiveTrueOrderByEventDateAsc();
//    List<Event> findByCategoryAndIsActiveTrueOrderByEventDateAsc(String category);
//    List<Event> findByEventDateAfterAndIsActiveTrueOrderByEventDateAsc(LocalDateTime date);
//    
//    @Query("SELECT e FROM Event e LEFT JOIN e.bookings b WHERE b.bookingStatus = 'CONFIRMED' " +
//           "GROUP BY e.id ORDER BY SUM(b.seatsBooked) DESC, COUNT(b.id) DESC")
//    List<Event> findTop3MostBookedEvents();
//
//    // NEW: Search events by title, venue, or category (case-insensitive)
//    @Query("SELECT e FROM Event e WHERE " +
//           "(LOWER(e.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
//           "OR LOWER(e.venue) LIKE LOWER(CONCAT('%', :query, '%')) " +
//           "OR LOWER(e.category) LIKE LOWER(CONCAT('%', :query, '%'))) " +
//           "AND e.isActive = true " +
//           "ORDER BY e.eventDate ASC")
//    List<Event> searchEvents(@Param("query") String query);
//}


package com.eventconnect.repository;

import com.eventconnect.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository // Marks this interface as a Spring Data repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Get all active events, sorted by event date ascending
    List<Event> findByIsActiveTrueOrderByEventDateAsc();

    // Get all active events of a specific category, sorted by event date ascending
    List<Event> findByCategoryAndIsActiveTrueOrderByEventDateAsc(String category);

    // Get all active events after a specific date, sorted by event date ascending
    List<Event> findByEventDateAfterAndIsActiveTrueOrderByEventDateAsc(LocalDateTime date);

    // Get top 3 most booked events based on seats booked and number of bookings
    @Query("SELECT e FROM Event e LEFT JOIN e.bookings b WHERE b.bookingStatus = 'CONFIRMED' " +
           "GROUP BY e.id ORDER BY SUM(b.seatsBooked) DESC, COUNT(b.id) DESC")
    List<Event> findTop3MostBookedEvents();

    // Search active events by title, venue, or category (case-insensitive), sorted by event date
    @Query("SELECT e FROM Event e WHERE " +
           "(LOWER(e.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(e.venue) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(e.category) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "AND e.isActive = true " +
           "ORDER BY e.eventDate ASC")
    List<Event> searchEvents(@Param("query") String query);
}

