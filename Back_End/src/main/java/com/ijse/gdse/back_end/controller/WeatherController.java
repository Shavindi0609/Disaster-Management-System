package com.ijse.gdse.back_end.controller;

import com.ijse.gdse.back_end.dto.WeatherDTO;
import com.ijse.gdse.back_end.service.WeatherService;
import com.ijse.gdse.back_end.service.impl.WeatherServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    public ResponseEntity<?> getWeather(@RequestParam String city) {
        try {
            WeatherDTO weather = weatherService.getWeather(city);
            return ResponseEntity.ok(weather);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching weather: " + e.getMessage());
        }
    }
}
