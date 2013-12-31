package com.coderetreat.gol.engine.grid;

import com.coderetreat.gol.models.Cell;

public interface IGrid {

    Cell getCellForPosition(Cell.Position position);

    int getWidth();

    int getHeight();

    void init();
}
