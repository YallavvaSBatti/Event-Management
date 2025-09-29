# Event Connect - Scalable Event Booking Platform

A simplified event booking system built with React.js frontend and Java Spring Boot backend, featuring JWT authentication, rate limiting, and responsive design.

## 🚀 Features

- **Frontend (React.js)**
  - Responsive event listing with search and filtering
  - Detailed event pages with booking functionality
  - User authentication (login/register)
  - Clean, modern UI with dark theme
  - Mobile-friendly design

- **Backend (Spring Boot)**
  - RESTful APIs for events and bookings
  - JWT-based authentication with 15-minute token expiry
  - Rate limiting (max 5 bookings per user per minute)
  - Comprehensive error handling
  - MySQL database integration

- **Security**
  - Password encryption with BCrypt
  - JWT token validation
  - CORS configuration
  - Protected booking endpoints

## 🛠 Tech Stack

### Frontend
- React.js 18
- React Router DOM
- Axios for API calls
- CSS3 with custom design system

### Backend
- Java 17
- Spring Boot 3.2
- Spring Security
- Spring Data JPA
- JWT (JSON Web Tokens)
- MySQL Database

## 📋 Prerequisites

- Node.js 16+ and npm
- Java 17+
- Maven 3.6+
- MySQL 8.0+

## 🚀 Getting Started

### Database Setup

1. Create MySQL database:
\`\`\`sql
CREATE DATABASE event_connect;
\`\`\`

2. Run the schema creation script:
\`\`\`bash
mysql -u root -p event_connect < scripts/01-create-schema.sql
\`\`\`

3. Seed with sample data:
\`\`\`bash
mysql -u root -p event_connect < scripts/02-seed-data.sql
\`\`\`

### Backend Setup

1. Navigate to backend directory:
\`\`\`bash
cd backend
\`\`\`

2. Update `application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/event_connect
spring.datasource.username=your_username
spring.datasource.password=your_password
