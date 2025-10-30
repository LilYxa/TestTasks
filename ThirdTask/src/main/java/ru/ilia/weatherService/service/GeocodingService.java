package ru.ilia.weatherService.service;

import ru.ilia.weatherService.utils.Constants;
import ru.ilia.weatherService.utils.HttpClientUtil;
import ru.ilia.weatherService.utils.PropertiesConfigUtil;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public class GeocodingService {

    private ObjectMapper objectMapper = new ObjectMapper();

    public double[] getCoordinates(String city) throws IOException, InterruptedException {
        String json = HttpClientUtil.get(
                PropertiesConfigUtil.getProperty(Constants.GEO_URL) + city
        );
        JsonNode root = objectMapper.readTree(json);
        JsonNode results = root.get("results");
        if (!results.isArray() || results.isEmpty()) return null;
        JsonNode first = results.get(0);
        double latitude = first.get("latitude").asDouble();
        double longitude = first.get("longitude").asDouble();
        return new double[]{latitude, longitude};
    }
}
