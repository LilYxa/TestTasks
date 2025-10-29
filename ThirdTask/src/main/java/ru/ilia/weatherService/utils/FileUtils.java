package ru.ilia.weatherService.utils;

public class FileUtils {

    public static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("." ));
    }
}
