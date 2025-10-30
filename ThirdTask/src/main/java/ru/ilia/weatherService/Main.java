package ru.ilia.weatherService;


import com.sun.net.httpserver.HttpServer;
import ru.ilia.weatherService.handler.StaticFileHandler;
import ru.ilia.weatherService.handler.WeatherHttpHandler;
import ru.ilia.weatherService.service.CacheService;
import ru.ilia.weatherService.service.GeocodingService;
import ru.ilia.weatherService.service.WeatherApiService;
import ru.ilia.weatherService.utils.Constants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        Files.createDirectories(Paths.get(Constants.STATIC_BASE_DIR));

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/weather", new WeatherHttpHandler(
                new CacheService(),
                new GeocodingService(),
                new WeatherApiService()
        ));
        server.createContext("/", new StaticFileHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Сервер запущен по адресу: http://localhost:8080");
    }
}