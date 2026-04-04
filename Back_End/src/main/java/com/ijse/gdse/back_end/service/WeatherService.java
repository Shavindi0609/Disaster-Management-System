package com.ijse.gdse.back_end.service;

import com.ijse.gdse.back_end.dto.WeatherDTO;

public interface WeatherService {

    WeatherDTO getWeather(String city);

    String calculateRiskLevel(WeatherDTO weather);

    void checkAndSendAlert(WeatherDTO weather);

    String getDamagePrediction(WeatherDTO weather);
}
