package com.ijse.gdse.back_end.service.impl;

import com.ijse.gdse.back_end.dto.WeatherDTO;
import com.ijse.gdse.back_end.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private RestTemplate restTemplate;

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
}
