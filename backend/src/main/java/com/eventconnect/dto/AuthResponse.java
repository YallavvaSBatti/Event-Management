package com.eventconnect.dto;

//Data Transfer Object for sending authentication responses

public class AuthResponse {
	 private String token; // JWT token for authentication
	    private String email; // Email of the authenticated user
	    private String name;  // Full name of the authenticated user
    
	    // Constructor to initialize all fields
    public AuthResponse(String token, String email, String name) {
        this.token = token;
        this.email = email;
        this.name = name;
    }
    
    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
