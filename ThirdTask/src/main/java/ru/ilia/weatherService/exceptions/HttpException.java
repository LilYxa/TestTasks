package ru.ilia.weatherService.exceptions;

public class HttpException extends RuntimeException {

    public HttpException(String message) {
        super(message);
    }
}
