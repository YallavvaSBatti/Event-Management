package com.eventconnect.repository;

import com.eventconnect.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository // Marks this interface as a Spring Data repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by email, returns Optional<User>
    Optional<User> findByEmail(String email);

    // Check if a user exists with the given email
    boolean existsByEmail(String email);
}
