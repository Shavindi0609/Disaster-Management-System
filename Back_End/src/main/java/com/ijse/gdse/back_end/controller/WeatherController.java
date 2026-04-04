package com.ijse.gdse.back_end.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijse.gdse.back_end.dto.WeatherDTO;
import com.ijse.gdse.back_end.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
                weatherService.checkAndSendAlert(weather);
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
    private final String API_KEY = "7438063e23b335097fc960d4ba85bbb4";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    // ලංකාවේ සියලුම දිස්ත්‍රික්ක ලැයිස්තුව
    private final String[] cities = {
            "Colombo", "Gampaha", "Kalutara", "Kandy", "Matale", "Nuwara Eliya",
            "Galle", "Matara", "Hambantota", "Jaffna", "Kilinochchi", "Mannar",
            "Vavuniya", "Mullaitivu", "Batticaloa", "Ampara", "Trincomalee",
            "Kurunegala", "Puttalam", "Anuradhapura", "Polonnaruwa",
            "Badulla", "Monaragala", "Ratnapura", "Kegalle"
    };

    @Scheduled(fixedRate = 3600000) // පැයෙන් පැයට run වේ
    public void checkWeatherRisk() {
        for (String city : cities) {
            try {
                WeatherDTO weather = weatherService.getWeather(city);

                weatherService.checkAndSendAlert(weather);


//                return ResponseEntity.ok(response);
                System.out.println("Weather Alert send successfully: " + weather);
            } catch (Exception e) {
//                return ResponseEntity.status(500).body("Error: " + e.getMessage());
                System.out.println("Can't send alert: " + e.getMessage());
            }
        }
    }

    private void sendAlert(String city, String risk, double temp, String desc) {
        // මෙතනදී Alert එකක් යැවීමේ logic එක ලියන්න (Email, SMS හෝ Database notification)
//        weatherService.checkAndSendAlert(city, risk);
        System.out.println("⚠️ [ALERT] City: " + city);
        System.out.println("Risk: " + risk);
        System.out.println("Details: " + desc + " | Temp: " + temp + "°C");
        System.out.println("--------------------------------------");
    }
}
