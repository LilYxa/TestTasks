package ru.ilia.liquidSort.service;

import ru.ilia.liquidSort.exceptions.InvalidMoveException;
import ru.ilia.liquidSort.models.Tube;

import java.util.Deque;
import java.util.Iterator;

public class TubeService {

    public static <T> boolean isEmpty(Tube<T> tube) {
        return tube.getDrops().isEmpty();
    }

    public static <T> boolean isFull(Tube<T> tube) {
        return tube.getDrops().size() == tube.getCapacity();
    }

    public static <T> T topColor(Tube<T> tube) {
        if (tube.getDrops().isEmpty()) return null;
        return tube.getDrops().peekLast();
    }

    public static <T> int topCount(Tube<T> tube) {
        if (isEmpty(tube)) return 0;
        T color = topColor(tube);
        int count = 0;
        Iterator<T> it = tube.getDrops().descendingIterator();
        while (it.hasNext() && it.next().equals(color)) {
            count++;
        }
        return count;
    }

    public static <T> boolean isSolved(Tube<T> tube) {
        if (isEmpty(tube)) return true;
        Deque<T> drops = tube.getDrops();
        T color = drops.peekFirst();
        for (T c: drops) {
            if (!c.equals(color)) return false;
        }
        return true;
    }

    public static <T> boolean canPourInto(Tube<T> from, Tube<T> to) {
        if (isEmpty(from) || isFull(to)) return false;
        return isEmpty(to) || topColor(to).equals(topColor(from));
    }

    public static <T> void pourInto(Tube<T> from, Tube<T> to) throws InvalidMoveException {
        if (!canPourInto(from, to)) {
            throw new InvalidMoveException("Invalid move");
        }
        T color = topColor(from);
        int available = to.getCapacity() - to.getDrops().size();
        int toMove = Math.min(topCount(from), available);

        for (int i = 0; i < toMove; i++) {
            from.getDrops().removeLast();
            to.getDrops().addLast(color);
        }
    }
}
