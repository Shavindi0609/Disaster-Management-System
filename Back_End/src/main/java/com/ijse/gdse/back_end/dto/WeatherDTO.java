package com.ijse.gdse.back_end.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class WeatherDTO {

    private Main main;
    private List<Weather> weather;
    private Wind wind;
    private String name;

    public Main getMain() { return main; }
    public void setMain(Main main) { this.main = main; }

    public List<Weather> getWeather() { return weather; }
    public void setWeather(List<Weather> weather) { this.weather = weather; }

    public Wind getWind() { return wind; }
    public void setWind(Wind wind) { this.wind = wind; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public static class Main {
        private double temp;
        private double feels_like;
        private int humidity;

        public double getTemp() { return temp; }
        public void setTemp(double temp) { this.temp = temp; }

        public double getFeels_like() { return feels_like; }
        public void setFeels_like(double feels_like) { this.feels_like = feels_like; }

        public int getHumidity() { return humidity; }
        public void setHumidity(int humidity) { this.humidity = humidity; }
    }

    public static class Weather {
        private String description;

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class Wind {
        private double speed;

        public double getSpeed() { return speed; }
        public void setSpeed(double speed) { this.speed = speed; }
    }
}
