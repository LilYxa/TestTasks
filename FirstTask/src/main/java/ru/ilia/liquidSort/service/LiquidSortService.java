package ru.ilia.liquidSort.service;

import ru.ilia.liquidSort.models.Move;

import java.util.List;

public interface LiquidSortService<T> {

    List<Move> solve(T[][] initialState);
}
