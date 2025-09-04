package com.biketrip.biketrip_planner.repository;

import com.biketrip.biketrip_planner.classes.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
