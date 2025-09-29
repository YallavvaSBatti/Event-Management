package com.eventconnect.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class BookingRequest {
    @NotNull
    private Long eventId;
    
    @Positive
    private Integer seatsBooked;
    
    @NotNull
    @Positive
    private BigDecimal totalAmount;
    
    // Constructors
    public BookingRequest() {}
    
    public BookingRequest(Long eventId, Integer seatsBooked, BigDecimal totalAmount) {
        this.eventId = eventId;
        this.seatsBooked = seatsBooked;
        this.totalAmount = totalAmount;
    }
    
    // Getters and Setters
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    
    public Integer getSeatsBooked() { return seatsBooked; }
    public void setSeatsBooked(Integer seatsBooked) { this.seatsBooked = seatsBooked; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}
