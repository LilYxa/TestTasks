# ThirdTask — Погодный сервис (HTTP + кэш Redis)

HTTP-сервис на Java 17, который отдаёт погоду по городу, используя Open-Meteo API. Встроенный кэш на Redis снижает число запросов к внешнему API. Статика (простая страница) обслуживается тем же сервером.

## Требования
- Java 17+
- Maven 3.8+
- Redis 6+ (локально на `localhost:6379`)

## Конфигурация
Файл `src/main/resources/application.properties`:
- `WEATHER_URL`, `GEO_URL` — эндпоинты Open-Meteo (по умолчанию заданы)
- `REDIS_URL`, `REDIS_PORT` — адрес Redis (по умолчанию `localhost:6379`)

## Запуск
Запустите Redis на `localhost:6379`, затем:
```
mvn org.codehaus.mojo:exec-maven-plugin:3.1.0:java "-Dexec.mainClass=ru.ilia.weatherService.Main"
# Откройте: http://localhost:8080
```

После запуска:
- Откройте `http://localhost:8080` — статическая страница
- Запросы погоды — `/weather?city={НазваниеГорода}` (используется геокодер Open-Метео)

