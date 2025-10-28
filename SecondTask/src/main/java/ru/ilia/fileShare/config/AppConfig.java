package ru.ilia.fileShare.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AppConfig {
    public static final String UPLOAD_DIR = "uploads";
    public static final long EXPIRY_DAYS = 30;
    public static final long EXPIRY_MILLIS = EXPIRY_DAYS * 24 * 60 * 60 * 1000L;
    public static final long CLEANUP_INTERVAL_MILLIS = 24 * 60 * 60 * 1000L;
    public static final String STATIC_BASE_DIR = "static";

    public static Path getUploadPath() {
        return Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
    }
}
