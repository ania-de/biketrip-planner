package com.biketrip.biketrip_planner.service;

import com.biketrip.biketrip_planner.classes.User;
import com.biketrip.biketrip_planner.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthService {
    private final UserRepository users;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository users) { this.users = users; }

    public User register(String username, String email, String rawPassword, double weight) {
        users.findByEmail(email).ifPresent(u -> { throw new IllegalArgumentException("Email zajęty"); });
        users.findByUsername(username).ifPresent(u -> { throw new IllegalArgumentException("Login zajęty"); });

        User u = new User();
        u.setUsername(username.trim());
        u.setEmail(email.trim().toLowerCase());
        u.setPassword(encoder.encode(rawPassword)); // hash!
        u.setWeight(weight);
        return users.save(u);
    }

    public User login(String email, String rawPassword, HttpSession session) {
        User u = users.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Nieprawidłowy email/hasło"));
        if (!encoder.matches(rawPassword, u.getPassword())) {
            throw new IllegalArgumentException("Nieprawidłowy email/hasło");
        }
        session.setAttribute("uid", u.getId());
        return u;
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

    public User current(HttpSession session) {
        Object uid = session.getAttribute("uid");
        if (uid == null) return null;
        return users.findById((Long) uid).orElse(null);
    }
}