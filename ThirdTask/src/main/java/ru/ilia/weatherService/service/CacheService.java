package ru.ilia.weatherService.service;

import redis.clients.jedis.Jedis;
import ru.ilia.weatherService.utils.Constants;
import ru.ilia.weatherService.utils.PropertiesConfigUtil;

public class CacheService {

    public String get(String key) {
        try (Jedis jedis = new Jedis(
                PropertiesConfigUtil.getProperty(Constants.REDIS_URL),
                Integer.parseInt(PropertiesConfigUtil.getProperty(Constants.REDIS_PORT))
        )) {
            return jedis.get(key);
        } catch (Exception e) {
            System.err.println("Ошибка подключения к Redis: " + e.getMessage());
            return null;
        }
    }

    public void set(String key, int seconds, String value) {
        try (Jedis jedis = new Jedis(
                PropertiesConfigUtil.getProperty(Constants.REDIS_URL),
                Integer.parseInt(PropertiesConfigUtil.getProperty(Constants.REDIS_PORT))
        )) {
            jedis.setex(key, seconds, value);
        } catch (Exception e) {
            System.err.println("Ошибка сохранения в Redis: " + e.getMessage());
        }
    }
}
