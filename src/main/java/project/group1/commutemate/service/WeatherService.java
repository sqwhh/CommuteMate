package project.group1.commutemate.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import project.group1.commutemate.model.Weather;
import tools.jackson.databind.JsonNode;

/** Fetches current weather on Burnaby Mountain from the Open-Meteo API. */
@Service
public class WeatherService {

    private static final String URL =
            "https://api.open-meteo.com/v1/forecast"
            + "?latitude=49.2781&longitude=-122.9199"
            + "&current=temperature_2m,weather_code,wind_speed_10m,wind_direction_10m";

    private final RestClient restClient = RestClient.create();

    public Optional<Weather> getCurrentWeather() {
        try {
            JsonNode root = restClient.get()
                    .uri(URL)
                    .retrieve()
                    .body(JsonNode.class);

            if (root == null || !root.has("current")) {
                return Optional.empty();
            }
            JsonNode current = root.get("current");

            double temperature = current.get("temperature_2m").asDouble();
            int weatherCode = current.get("weather_code").asInt();
            double windSpeed = current.get("wind_speed_10m").asDouble();
            int windDirection = current.get("wind_direction_10m").asInt();

            Weather weather = new Weather(
                    temperature,
                    describe(weatherCode),
                    windSpeed,
                    compass(windDirection));
            return Optional.of(weather);
        } catch (Exception e) {
            return Optional.empty();  
        }
    }

   
    String describe(int code) {
        return switch (code) {
            case 0 -> "Clear sky";
            case 1, 2, 3 -> "Partly cloudy";
            case 45, 48 -> "Fog";
            case 51, 53, 55 -> "Drizzle";
            case 61, 63, 65 -> "Rain";
            case 71, 73, 75 -> "Snow";
            case 80, 81, 82 -> "Rain showers";
            case 85, 86 -> "Snow showers";
            case 95, 96, 99 -> "Thunderstorm";
            default -> "Unknown";
        };
    }

    String compass(int degrees) {
        String[] dirs = { "N", "NE", "E", "SE", "S", "SW", "W", "NW" };
        int index = (int) Math.round(degrees / 45.0) % 8;
        return dirs[index];
    }
}
