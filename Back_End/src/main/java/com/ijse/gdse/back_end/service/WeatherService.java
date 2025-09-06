package com.ijse.gdse.back_end.service;

import com.ijse.gdse.back_end.dto.WeatherDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Autowired
    private RestTemplate restTemplate;

    private final String API_KEY = "7438063e23b335097fc960d4ba85bbb4";  // ✅ Replace with your OpenWeatherMap key
    private final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public WeatherDTO getWeather(String city) {
        String url = BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric";
        return restTemplate.getForObject(url, WeatherDTO.class);
    }
}
