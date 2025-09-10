package com.ijse.gdse.back_end.controller;

import com.ijse.gdse.back_end.dto.APIResponse;
import com.ijse.gdse.back_end.entity.User;
import com.ijse.gdse.back_end.repository.UserRepository;
import com.ijse.gdse.back_end.service.DonorService;
import com.ijse.gdse.back_end.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth/users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<APIResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(
                new APIResponse(200, "User List", users)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);  // Service method use කරනවා
            return ResponseEntity.ok(new APIResponse(200, "User deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new APIResponse(404, e.getMessage(), id));
        }
    }


    @GetMapping("/me")
    public ResponseEntity<APIResponse> getLoggedInUser(Authentication authentication) {
        String principal = authentication.getName(); // JWT -> principal (email)

        Optional<User> userOpt = userRepository.findByEmail(principal); // 🔹 use email here

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(
                    new APIResponse(404, "User not found", null)
            );
        }

        return ResponseEntity.ok(
                new APIResponse(200, "User profile loaded", userOpt.get())
        );
    }



    @PutMapping("/{id}")
    public ResponseEntity<APIResponse> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(
                    new APIResponse(200, "User updated successfully", updatedUser)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new APIResponse(500, "Error updating user: " + e.getMessage(), null)
            );
        }
    }



}
