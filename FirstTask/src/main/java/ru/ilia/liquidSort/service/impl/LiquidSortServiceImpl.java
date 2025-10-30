package ru.ilia.liquidSort.service.impl;

import ru.ilia.liquidSort.exceptions.InvalidMoveException;
import ru.ilia.liquidSort.models.Move;
import ru.ilia.liquidSort.models.State;
import ru.ilia.liquidSort.models.Tube;
import ru.ilia.liquidSort.service.LiquidSortService;
import ru.ilia.liquidSort.service.TubeService;
import ru.ilia.liquidSort.utils.TubeUtils;

import java.util.*;

public class LiquidSortServiceImpl<T> implements LiquidSortService<T> {

    public List<Move> solve(T[][] initialState) {
        if (initialState == null || initialState.length == 0) {
            return Collections.emptyList();
        }

        int numTubes = initialState.length;
        int capacity = initialState[0].length;

        List<Tube<T>> tubes = TubeUtils.parseInitialTubes(initialState, capacity);

        if (TubeUtils.isSolved(tubes)) {
            return Collections.emptyList();
        }

        Queue<State<T>> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        State<T> initialStateObj = new State<>(tubes, new ArrayList<>());
        queue.offer(initialStateObj);
        visited.add(TubeUtils.stateHash(tubes));

        while (!queue.isEmpty()) {
            State<T> current = queue.poll();

            for (int from = 0; from < numTubes; from++) {
                for (int to = 0; to < numTubes; to++) {
                    if (from == to) continue;

                    if (!current.getMoves().isEmpty()) {
                        Move last = current.getMoves().get(current.getMoves().size() - 1);
                        if (last.getFrom() == to && last.getTo() == from) continue;
                    }

                    List<Tube<T>> newTubes = TubeUtils.cloneTubes(current.getTubes());

                    if (TubeService.canPourInto(newTubes.get(from), newTubes.get(to))) {
                        try {
                            TubeService.pourInto(newTubes.get(from), newTubes.get(to));

                            String hash = TubeUtils.stateHash(newTubes);
                            if (!visited.contains(hash)) {
                                visited.add(hash);

                                List<Move> newMoves = new ArrayList<>(current.getMoves());
                                newMoves.add(new Move(from, to));

                                if (TubeUtils.isSolved(newTubes)) {
                                    System.out.println("РЕШЕННОЕ СОСТОЯНИЕ:");
                                    TubeUtils.printTubes(newTubes);
                                    return newMoves;
                                }

                                queue.offer(new State<>(newTubes, newMoves));
                            }
                        } catch (InvalidMoveException e) {
                            System.out.println(e.getMessage());
                        }
                    }

                }
            }
        }

        return null;

    }
}