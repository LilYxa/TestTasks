package ru.ilia.liquidSort.utils;

import ru.ilia.liquidSort.models.Tube;
import ru.ilia.liquidSort.service.TubeService;

import java.util.*;

public class TubeUtils {

    public static <T> List<Tube<T>> cloneTubes(List<Tube<T>> tubes) {
        List<Tube<T>> clone = new ArrayList<>();
        for (Tube<T> tube : tubes) {
            Tube<T> newTube = new Tube<>(tube.getCapacity());
            tube.getDrops().forEach(color -> {
                newTube.getDrops().addLast(color);
            });
            clone.add(newTube);
        }
        return clone;
    }

    public static <T> List<Tube<T>> parseInitialTubes(T[][] initialState, int capacity) {
        List<Tube<T>> tubes = new ArrayList<>();
        for (T[] row : initialState) {
            Tube<T> tube = new Tube<>(capacity);

            Arrays.stream(row).forEach(color -> {
                if (color != null) {
                    tube.getDrops().addLast(color);
                }
            });
            tubes.add(tube);
        }

        return tubes;
    }

    public static <T> void printTubes(List<Tube<T>> tubes) {
        for (int i = 0; i < tubes.size(); i++) {
            System.out.printf("Пробирка %d: %s\n", i, tubes.get(i).toString());
        }
    }

    public static <T> boolean isSolved(List<Tube<T>> tubes) {
        if (tubes.isEmpty()) return true;

        int capacity = tubes.get(0).getCapacity();
        int nonEmptyTubes = 0;
        Set<T> colors = new HashSet<>();

        for (Tube<T> tube : tubes) {
            if (TubeService.isEmpty(tube)) {
                continue;
            }

            if (!TubeService.isSolved(tube)) {
                return false;
            }

            if (tube.asList().size() != capacity) {
                return false;
            }

            colors.add(TubeService.topColor(tube));
            nonEmptyTubes++;
        }

        return nonEmptyTubes == colors.size();
    }

    public static <T> String stateHash(List<Tube<T>> tubes) {
        List<String> tubeStrings = new ArrayList<>();
        for (Tube<T> tube : tubes) {
            String content = String.join(",", tube.asList().stream()
                    .map(String::valueOf)
                    .toList());
            tubeStrings.add(content);
        }
        Collections.sort(tubeStrings);
        return String.join("|", tubeStrings);
    }
}
