package ru.ilia.liquidSort.models;

import java.util.*;

public class Tube<T> {

    private final int capacity;
    private final Deque<T> drops;

    public Tube(int capacity) {
        this.capacity = capacity;
        this.drops = new ArrayDeque<>();
    }

    public List<T> asList() {
        return new ArrayList<>(drops);
    }

    public int getCapacity() {
        return capacity;
    }

    public Deque<T> getDrops() {
        return drops;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tube<?> tube = (Tube<?>) o;
        return capacity == tube.capacity && Objects.equals(drops, tube.drops);
    }

    @Override
    public int hashCode() {
        return Objects.hash(capacity, drops);
    }

    @Override
    public String toString() {
        return drops.toString();
    }
}
