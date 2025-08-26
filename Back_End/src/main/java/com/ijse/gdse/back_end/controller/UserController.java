package com.ijse.gdse.back_end.controller;

import com.ijse.gdse.back_end.dto.APIResponse;
import com.ijse.gdse.back_end.entity.User;
import com.ijse.gdse.back_end.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<APIResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(
                new APIResponse(200, "User List", users)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok(
                new APIResponse(200, "User deleted successfully", null)
        );
    }
}
