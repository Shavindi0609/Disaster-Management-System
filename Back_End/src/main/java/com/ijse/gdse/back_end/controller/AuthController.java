package com.ijse.gdse.back_end.controller;

import com.ijse.gdse.back_end.dto.APIResponse;
import com.ijse.gdse.back_end.dto.AuthDTO;
import com.ijse.gdse.back_end.dto.RegisterDTO;
import com.ijse.gdse.back_end.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<APIResponse> registerUser(
            @RequestBody RegisterDTO registerDTO){
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "User Registered Successfully",
                        authService.register(registerDTO)
                )
        );
    }


    @PostMapping("/login")
    public ResponseEntity<APIResponse> login(@RequestBody AuthDTO authDTO) {
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "OK",
                        authService.authenticate(authDTO)
                )
        );
    }
}
