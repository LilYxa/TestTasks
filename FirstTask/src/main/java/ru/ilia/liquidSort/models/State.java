package ru.ilia.liquidSort.models;

import java.util.List;

public class State<T> {
    private List<Tube<T>> tubes;
    private List<Move> moves;

    public State(List<Tube<T>> tubes, List<Move> moves) {
        this.tubes = tubes;
        this.moves = moves;
    }

    public List<Tube<T>> getTubes() {
        return tubes;
    }

    public List<Move> getMoves() {
        return moves;
    }
}
