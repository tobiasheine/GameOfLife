package com.coderetreat.gol.engine.concurrent;

import com.coderetreat.gol.engine.IGridEngine;
import com.coderetreat.gol.grid.cell.Cell;

public interface GridWorker extends Runnable, IGridEngine{
    void addPosition(Cell.Position position);
}
