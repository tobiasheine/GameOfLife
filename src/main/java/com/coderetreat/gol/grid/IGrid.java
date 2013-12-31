package com.coderetreat.gol.grid;

import com.coderetreat.gol.grid.cell.Cell;

public interface IGrid {

    Cell getCellForPosition(Cell.Position position);

    int getWidth();

    int getHeight();

    void init();
}
