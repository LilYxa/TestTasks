package ru.ilia;

import ru.ilia.liquidSort.models.Move;
import ru.ilia.liquidSort.service.LiquidSortService;
import ru.ilia.liquidSort.service.impl.LiquidSortServiceImpl;
import ru.ilia.liquidSort.utils.ConsoleStateReader;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Пример 1: Цвета как строки");
        exampleWithStrings();

        System.out.println("\nПример 2: Цвета как числа");
        exampleWithIntegers();

        System.out.println("\nПример 3: Цвета как символы");
        exampleWithCharacters();

        System.out.println("\nПример 4: Ввод состояния из консоли (строки)");
        exampleFromConsole();

    }

    private static void exampleWithStrings() {
        String[][] initialState = {
                {"R", "B", "G", "Y"},
                {"G", "Y", "R", "B"},
                {"B", "G", "Y", "R"},
                {"Y", "R", "B", "G"},
                {},
                {}
        };

        System.out.println("Начальное состояние:");
        printState(initialState);

        LiquidSortService<String> solver = new LiquidSortServiceImpl<>();
        List<Move> solution = solver.solve(initialState);

        printSolutionResult(solution);
    }

    private static void exampleWithIntegers() {
        Integer[][] initialState = {
                {2, 10, 4, 4},
                {1, 8, 12, 8},
                {10, 7, 5, 9},
                {5, 3, 2, 5},
                {6, 11, 8, 7},
                {12, 12, 1, 2},
                {4, 7, 8, 11},
                {10, 11, 3, 1},
                {10, 7, 9, 9},
                {6, 2, 6, 11},
                {4, 6, 9, 3},
                {5, 3, 12, 1},
                {},
                {},
        };

        System.out.println("Начальное состояние:");
        printState(initialState);

        LiquidSortService<Integer> solver = new LiquidSortServiceImpl<>();
        List<Move> solution = solver.solve(initialState);

        printSolutionResult(solution);
    }

    private static void exampleWithCharacters() {
        Character[][] initialState = {
                {'A', 'B', 'C'},
                {'C', 'A', 'B'},
                {'B', 'C', 'A'},
                {},
                {}
        };

        System.out.println("Начальное состояние:");
        printState(initialState);

        LiquidSortService<Character> solver = new LiquidSortServiceImpl<>();
        List<Move> solution = solver.solve(initialState);

        printSolutionResult(solution);
    }

    private static <T> void printState(T[][] state) {
        for (int i = 0; i < state.length; i++) {
            System.out.print("Пробирка " + i + ": [");
            for (int j = 0; j < state[i].length; j++) {
                if (j > 0) System.out.print(", ");
                System.out.print(state[i][j] == null ? " " : state[i][j]);
            }
            System.out.println("]");
        }
    }

    private static void printSolutionResult(List<Move> solution) {
        if (solution == null) {
            System.out.println("\nРешение не найдено!");
        } else if (solution.isEmpty()) {
            System.out.println("\nГоловоломка уже решена!");
        } else {
            System.out.println("\nРешение найдено за " + solution.size() + " ходов:");
            printSolution(solution);
        }
    }

    private static void printSolution(List<Move> moves) {
        int count = 0;
        for (Move move : moves) {
            System.out.print(move);
            count++;
            if (count % 8 == 0) {
                System.out.println();
            } else {
                System.out.print(" ");
            }
        }
        if (count % 8 != 0) {
            System.out.println();
        }
    }

    private static void exampleFromConsole() {
        String[][] initialState = ConsoleStateReader.readStateFromConsole(s -> s, 
                len -> new String[len], 
                len -> new String[len][]);
        if (initialState == null) {
            System.out.println("Не удалось прочитать состояние из консоли.");
            return;
        }

        System.out.println("Начальное состояние (из консоли):");
        printState(initialState);

        LiquidSortService<String> solver = new LiquidSortServiceImpl<>();
        List<Move> solution = solver.solve(initialState);

        printSolutionResult(solution);
    }

    
}