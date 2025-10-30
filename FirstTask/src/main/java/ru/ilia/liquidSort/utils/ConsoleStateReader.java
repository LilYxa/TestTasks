package ru.ilia.liquidSort.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;

public final class ConsoleStateReader {
    private ConsoleStateReader() { }

    public static <T> T[][] readStateFromConsole(Function<String, T> mapper,
                                                 IntFunction<T[]> rowFactory,
                                                 IntFunction<T[][]> matrixFactory) {
        System.out.println("Введите состояние пробирок. Одна пробирка — одна строка.");
        System.out.println("Разделители значений: пробел, запятая или точка с запятой.");
        System.out.println("Пустая пробирка — пустая строка или []");
        System.out.println("Оставьте строку пустой дважды (два Enter подряд), чтобы завершить ввод.\n");

        List<T[]> rows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            int consecutiveEmptyLines = 0;
            while (true) {
                System.out.print("> ");
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    consecutiveEmptyLines++;
                    if (consecutiveEmptyLines >= 2) {
                        break;
                    }
                    continue;
                } else {
                    consecutiveEmptyLines = 0;
                }

                if (trimmed.startsWith("#")) {
                    continue;
                }

                String[] parts = trimmed.split("[\\s,;]+");
                if (parts.length == 1 && (parts[0].isEmpty() || parts[0].equals("[]"))) {
                    rows.add(rowFactory.apply(0));
                } else {
                    T[] row = rowFactory.apply(parts.length);
                    for (int i = 0; i < parts.length; i++) {
                        row[i] = mapper.apply(parts[i]);
                    }
                    rows.add(row);
                }
            }
        } catch (IOException e) {
            return null;
        }

        T[][] state = matrixFactory.apply(rows.size());
        for (int i = 0; i < rows.size(); i++) {
            state[i] = rows.get(i);
        }
        return state;
    }
}


