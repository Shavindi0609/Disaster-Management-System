package com.ijse.gdse.back_end.service.impl;

import com.ijse.gdse.back_end.dto.WeatherDTO;
import com.ijse.gdse.back_end.entity.AlertSubscriber;
import com.ijse.gdse.back_end.repository.AlertSubscriberRepositor;
import com.ijse.gdse.back_end.service.AlertService;
import com.ijse.gdse.back_end.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private RestTemplate restTemplate;
    private final AlertService alertService;

    @Autowired
    private AlertSubscriberRepositor subscriberRepository; // මෙය අලුතින් එක් කරන්න
    private final String API_KEY = "7438063e23b335097fc960d4ba85bbb4";  // ✅ Replace with your OpenWeatherMap key
    private final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public WeatherDTO getWeather(String city) {
        String url = BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric";
        return restTemplate.getForObject(url, WeatherDTO.class);
    }

    // WeatherServiceImpl.java තුළට එකතු කරන්න

    public String calculateRiskLevel(WeatherDTO weather) {
        double windSpeed = weather.getWind().getSpeed();
        int humidity = weather.getMain().getHumidity();
        String desc = weather.getWeather().get(0).getDescription().toLowerCase();

        // 1. දැඩි සුළං අවදානම (Cyclone/Storm Risk)
        if (windSpeed > 15.0) {
            return "HIGH RISK: Severe Storm/Cyclone Warning!";

        }

        // 2. ගංවතුර අවදානම (Flood Risk based on rain and humidity)
        if (desc.contains("heavy rain") || (humidity > 85 && desc.contains("rain"))) {
            return "HIGH RISK: Potential Flash Flood Warning!";
        }

        // 3. මධ්‍යම අවදානම (Moderate Risk)
        if (windSpeed > 10.0 || humidity > 75) {
            return "MODERATE RISK: Stay alert for changing weather.";
        }

        return "LOW RISK: Weather conditions are stable.";
    }

//    @Override
//    public void checkAndSendAlert(String email, WeatherDTO weather) {
//        String risk = calculateRiskLevel(weather);
//        String city = weather.getName();
//
//        if (risk.contains("HIGH RISK")) {
//            // 1. Email එක යැවීම
//            alertService.sendEmergencyEmail(email, city, risk);
//
//            // 2. AlertService එකේ ඇති method එක හරහා සියලුම දෙනාට SMS යැවීම
//            try {
//                alertService.sendBulkSms(city, risk);
//                System.out.println("Emergency bulk SMS triggered for " + city);
//            } catch (Exception e) {
//                System.err.println("Failed to trigger bulk SMS: " + e.getMessage());
//            }
//        }
//    }

    @Override
    public void checkAndSendAlert(String email, WeatherDTO weather) {
        String risk = calculateRiskLevel(weather); // උදා: "HIGH RISK: Severe Storm!"
        String city = weather.getName();

        if (risk.contains("HIGH RISK")) {
            alertService.sendEmergencyEmail(email, city, risk);

            // පණිවිඩය සකස් කර Bulk SMS එක Trigger කරන්න
            String alertMsg = "🚨 NDMS EMERGENCY: " + risk + " in " + city + ". Take care!";
            alertService.sendBulkSms(city, alertMsg);
        }
    }

    // WeatherServiceImpl.java
    @Override
    public String getDamagePrediction(WeatherDTO weather) {
        double windSpeed = weather.getWind().getSpeed();
        int humidity = weather.getMain().getHumidity();
        String desc = weather.getWeather().get(0).getDescription().toLowerCase();

        // සරල අනාවැකි Algorithm එකක්
        if (windSpeed > 20.0 || desc.contains("storm")) {
            return "High Probability of structural damage to temporary buildings and fallen trees.";
        } else if (desc.contains("heavy rain") && humidity > 90) {
            return "Significant flood risk detected. Low-lying areas may experience 10-15cm of water logging.";
        } else if (humidity > 80) {
            return "Minor agricultural impact expected due to high moisture levels.";
        }

        return "No significant environmental damage predicted based on current data.";
    }


}
