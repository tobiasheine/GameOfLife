package com.coderetreat.gol.grid;

import com.coderetreat.gol.grid.IGrid;
import com.coderetreat.gol.grid.cell.Cell;

public interface IGameOfLifeCanvas {
    void drawGrid();

    IGrid getGrid();

    void changeCellAtPositionManually(Cell.Position position);
}
