package com.ijse.gdse.back_end.controller;

import com.ijse.gdse.back_end.dto.AlertDTO;
import com.ijse.gdse.back_end.entity.AlertSubscriber;
import com.ijse.gdse.back_end.repository.AlertSubscriberRepositor;
import com.ijse.gdse.back_end.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alerts")
@CrossOrigin
public class AlertController {

    @Autowired
    private AlertService alertService; // Service එක හරහා දත්ත යැවීම

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AlertDTO alertDTO) {
        String result = alertService.saveSubscriber(alertDTO);

        if (result.contains("Invalid")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }
}
