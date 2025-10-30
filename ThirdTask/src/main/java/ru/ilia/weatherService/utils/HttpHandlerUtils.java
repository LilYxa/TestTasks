package ru.ilia.weatherService.utils;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpHandlerUtils {

    public static Map<String, String> parseQueryString(String query) {
        var map = new HashMap<String, String>();
        if (query == null) return map;
        for (String param : query.split("&")) {
            String[] pair = param.split("=", 2);
            String key = pair[0];
            String value = pair.length == 2 ? pair[1] : "";
            map.put(key, URLDecoder.decode(value, StandardCharsets.UTF_8));
        }
        return map;
    }

    public static void sendText(HttpExchange exchange, int code, String text) throws IOException {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        exchange.sendResponseHeaders(code, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }
}
