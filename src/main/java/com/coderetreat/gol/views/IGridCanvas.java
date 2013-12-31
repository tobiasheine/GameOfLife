package com.coderetreat.gol.views;

import com.coderetreat.gol.engine.grid.IGrid;
import com.coderetreat.gol.models.Cell;

public interface IGridCanvas {
    void drawGrid();

    IGrid getGrid();

    void changeCellAtPositionManually(Cell.Position position);
}
