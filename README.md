# Тестовое задание — запуск из командной строки

Репозиторий содержит три независимых Maven-проекта (Java 17):
- `FirstTask` — решение головоломки «Liquid Sort» 
- `SecondTask` — мини-файлообменник (HTTP сервер на 8080)
- `ThirdTask` — погодный сервис с кэшем Redis (HTTP сервер на 8080)

## Требования
- Java 17+
- Maven 3.8+
- (для `ThirdTask`) Redis 6+ на `localhost:6379`

## Быстрый старт

### FirstTask
```
mvn -f ./FirstTask/pom.xml package
java -cp ./FirstTask/target/FirstTask-1.0-SNAPSHOT.jar ru.ilia.Main
```

### SecondTask
```
cd ./SecondTask
mvn package
java -cp ./target/SecondTask-1.0-SNAPSHOT.jar ru.ilia.fileShare.Main
# Откройте: http://localhost:8080
```

### ThirdTask
Запустите Redis на `localhost:6379`, затем:
```
cd .\ThirdTask
mvn org.codehaus.mojo:exec-maven-plugin:3.1.0:java "-Dexec.mainClass=ru.ilia.weatherService.Main"
# Откройте: http://localhost:8080
```

Подробности см. в `README.md` внутри каждого модуля.