package ru.ilia.weatherService.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesConfigUtil {
	private static String configPath = "";
	private static final Properties config = new Properties();

	public PropertiesConfigUtil() {

	}

	public static String getConfigPath() {
		return configPath;
	}

	public static void setConfigPath(String configPath) {
		PropertiesConfigUtil.configPath = configPath;
	}

	public static Properties getConfiguration() throws IOException {
		if (config.isEmpty()) {
			loadConfiguration();
		}
		return config;
	}

	private static void loadConfiguration() throws IOException {
		File file = new File(configPath.isEmpty() ? Constants.PROPERTIES_CONFIG_PATH : configPath);
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			config.load(fileInputStream);
		} catch (IOException e) {
			throw new IOException(e);
		}
	}

	public static String getProperty(String key) {
		try {
			return getConfiguration().getProperty(key);
		} catch (IOException e) {
            System.err.printf("Error: %s", e.getMessage());
		}
		return "";
	}
}
