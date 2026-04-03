package com.ijse.gdse.back_end.controller;

import com.ijse.gdse.back_end.dto.WeatherDTO;
import com.ijse.gdse.back_end.service.WeatherService;
import com.ijse.gdse.back_end.service.impl.WeatherServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

//    @GetMapping
//    public ResponseEntity<?> getWeather(@RequestParam String city) {
//        try {
//            WeatherDTO weather = weatherService.getWeather(city);
//            return ResponseEntity.ok(weather);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Error fetching weather: " + e.getMessage());
//        }
//    }

//    @GetMapping
//    public ResponseEntity<?> getWeather(@RequestParam String city) {
//        try {
//            WeatherDTO weather = weatherService.getWeather(city);
//            String riskLevel = weatherService.calculateRiskLevel(weather); // නව ලොජික් එක
//
//            // Response එකක් ලෙස Weather දත්ත සහ Risk Level එක යන දෙකම යවන්න
//            Map<String, Object> response = new HashMap<>();
//            response.put("weatherData", weather);
//            response.put("alert", riskLevel);
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Error: " + e.getMessage());
//        }
//    }

//    @GetMapping
//    public ResponseEntity<?> getWeather(@RequestParam String city, @RequestParam(required = false) String email) {
//        try {
//            WeatherDTO weather = weatherService.getWeather(city);
//            String riskLevel = weatherService.calculateRiskLevel(weather);
//
//            // Email එකක් එවලා තියෙනවා නම් alert එකක් යැවිය යුතුදැයි බලන්න
//            if (email != null && !email.isEmpty()) {
//                weatherService.checkAndSendAlert(email, weather);
//            }
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("weatherData", weather);
//            response.put("alert", riskLevel);
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Error: " + e.getMessage());
//        }
//    }

    @GetMapping
    public ResponseEntity<?> getWeather(@RequestParam String city, @RequestParam(required = false) String email) {
        try {
            WeatherDTO weather = weatherService.getWeather(city);
            String riskLevel = weatherService.calculateRiskLevel(weather);

            // පරිශීලකයා ලොග් වී ඇත්නම් (Email එකක් තිබේ නම්) පමණක් Alert එක යවන්න
            if (email != null && !email.isEmpty() && !email.equals("undefined") && !email.equals("null")) {
                weatherService.checkAndSendAlert(email, weather);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("weatherData", weather);
            response.put("alert", riskLevel);
            response.put("prediction", weatherService.getDamagePrediction(weather));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
