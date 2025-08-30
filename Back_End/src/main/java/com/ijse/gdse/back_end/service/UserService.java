package com.ijse.gdse.back_end.service;

import com.ijse.gdse.back_end.entity.User;
import com.ijse.gdse.back_end.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Get user by username
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Add new user
    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            // encode password before saving
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(existingUser);
    }



    // Delete user
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Count users
    public long countUsers() {
        return userRepository.count();
    }
}
