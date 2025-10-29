package ru.ilia.weatherService.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.ilia.weatherService.service.CacheService;
import ru.ilia.weatherService.service.GeocodingService;
import ru.ilia.weatherService.service.WeatherApiService;
import ru.ilia.weatherService.utils.HttpHandlerUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class WeatherHttpHandler implements HttpHandler {

    private final CacheService cacheService;
    private final GeocodingService geocodingService;
    private final WeatherApiService weatherApiService;

    public WeatherHttpHandler(CacheService cacheService,
                              GeocodingService geocodingService,
                              WeatherApiService weatherApiService) {
        this.cacheService = cacheService;
        this.geocodingService = geocodingService;
        this.weatherApiService = weatherApiService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            HttpHandlerUtils.sendText(exchange, 405, "Метод не разрешен");
            return;
        }

        Map<String, String> params = HttpHandlerUtils.parseQueryString(exchange.getRequestURI().getQuery());
        String city = params.get("city");
        if (city == null || city.trim().isEmpty()) {
            HttpHandlerUtils.sendText(exchange, 400, "Некорректный параметр city");
            return;
        }

        try {
            String cacheKey = "weather:" + city.toLowerCase();
            String cacheValue = cacheService.get(cacheKey);
            String weatherJson;

            if (cacheValue != null) {
                weatherJson = cacheValue;
            } else {
                double[] coords = geocodingService.getCoordinates(city);
                if (coords == null) {
                    HttpHandlerUtils.sendText(exchange, 404, "Город не найден");
                    return;
                }
                weatherJson = weatherApiService.getWeatherJson(coords[0], coords[1]);
                if (weatherJson == null) {
                    HttpHandlerUtils.sendText(exchange, 502, "Ошибка API OpenMeteo");
                    return;
                }
                // Сохраняем в кэш
                cacheService.set(cacheKey, 900, weatherJson);
            }

            byte[] body = weatherJson.getBytes();
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, body.length);
            try (OutputStream os = exchange.getResponseBody();
                 exchange) {
                os.write(body);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            HttpHandlerUtils.sendText(exchange, 500, "Ошибка сервера: " + e.getMessage());
        }
    }
}
