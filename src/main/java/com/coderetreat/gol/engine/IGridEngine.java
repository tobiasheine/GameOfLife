package com.coderetreat.gol.engine;

import com.coderetreat.gol.engine.grid.IGrid;

public interface IGridEngine {
    void processNextGeneration(IGrid grid);
}
