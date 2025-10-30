package ru.ilia.fileShare.utils;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class HttpUtils {

    public static void sendErrorResponse(HttpExchange exchange, int code, String message) throws IOException {
        byte[] msg = message.getBytes();
        exchange.sendResponseHeaders(code, msg.length);
        try (OutputStream os = exchange.getResponseBody();
             exchange) {
            os.write(msg);
        }
    }
}
