package ru.ilia.weatherService.service;

import ru.ilia.weatherService.utils.Constants;
import ru.ilia.weatherService.utils.HttpClientUtil;
import ru.ilia.weatherService.utils.PropertiesConfigUtil;

import java.io.IOException;

public class WeatherApiService {

    public String getWeatherJson(double latitude, double longitude) throws IOException, InterruptedException {
        String url = String.format(
                PropertiesConfigUtil.getProperty(Constants.WEATHER_URL),
                latitude,
                longitude
        );

        return HttpClientUtil.get(url);
    }
}
