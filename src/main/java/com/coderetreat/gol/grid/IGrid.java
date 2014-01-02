package com.coderetreat.gol.grid;

import com.coderetreat.gol.grid.cell.Cell;
import java.util.List;

public interface IGrid {

    Cell getCellForPosition(Cell.Position position);

    int getWidth();

    int getHeight();

    void init();

    List<Cell> getCellNeighbours(Cell.Position position);
}
