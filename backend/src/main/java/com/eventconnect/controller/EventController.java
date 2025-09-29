package com.eventconnect.controller;

import com.eventconnect.entity.Event;
import com.eventconnect.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:3000")
public class EventController {
    
    @Autowired
    private EventRepository eventRepository;
    
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventRepository.findByEventDateAfterAndIsActiveTrueOrderByEventDateAsc(LocalDateTime.now());
        return ResponseEntity.ok(events);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventRepository.findById(id)
            .map(event -> ResponseEntity.ok(event))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Event>> getEventsByCategory(@PathVariable String category) {
        List<Event> events = eventRepository.findByCategoryAndIsActiveTrueOrderByEventDateAsc(category);
        return ResponseEntity.ok(events);
    }
    
    @GetMapping("/popular")
    public ResponseEntity<List<Event>> getPopularEvents() {
        List<Event> events = eventRepository.findTop3MostBookedEvents();
        return ResponseEntity.ok(events.subList(0, Math.min(3, events.size())));
    }
    
    // NEW: Search events by title, venue, or category
    @GetMapping("/search")
    public ResponseEntity<List<Event>> searchEvents(@RequestParam("q") String query) {
        List<Event> results = eventRepository.searchEvents(query);
        return ResponseEntity.ok(results);
    }
    
 // CREATE new event
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        event.setCreatedAt(LocalDateTime.now());
        event.setIsActive(true);
        Event savedEvent = eventRepository.save(event);
        return ResponseEntity.ok(savedEvent);
    }

    // UPDATE existing event
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event updatedEvent) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Event existingEvent = optionalEvent.get();
        existingEvent.setTitle(updatedEvent.getTitle());
        existingEvent.setCategory(updatedEvent.getCategory());
        existingEvent.setVenue(updatedEvent.getVenue());
        existingEvent.setEventDate(updatedEvent.getEventDate());
        existingEvent.setDescription(updatedEvent.getDescription());
        existingEvent.setIsActive(updatedEvent.getIsActive());
        Event savedEvent = eventRepository.save(existingEvent);
        return ResponseEntity.ok(savedEvent);
    }

    // DELETE event
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (!eventRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        eventRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
