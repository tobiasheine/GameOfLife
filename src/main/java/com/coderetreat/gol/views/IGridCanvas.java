package com.coderetreat.gol.views;

import com.coderetreat.gol.engine.IGrid;
import com.coderetreat.gol.models.Cell;

public interface IGridCanvas {
    void drawGrid();

    IGrid getGrid();

    void changeManuallyLifeOfCell(Cell.Position position);
}
