# SecondTask — Мини-файлообменник (HTTP сервер)

Легковесный HTTP-сервер на Java 17 для загрузки, скачивания и просмотра статистики файлов. На старте создаёт каталоги и обслуживает простую статическую страницу.

## Требования
- Java 17+
- Maven 3.8+

## Сборка и запуск
```
mvn package
java -cp ./target/SecondTask-1.0-SNAPSHOT.jar ru.ilia.fileShare.Main
# Откройте: http://localhost:8080
```

После запуска:
- Откройте `http://localhost:8080` для веб-страницы
- Загрузка: форма на главной странице
- Скачивание: `http://localhost:8080/download/{id}`
- Статистика: `http://localhost:8080/stats`

