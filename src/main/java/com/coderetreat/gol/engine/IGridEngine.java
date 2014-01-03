package com.coderetreat.gol.engine;

import com.coderetreat.gol.grid.IGrid;

public interface IGridEngine {
    void processNextGeneration(final IGrid grid);

    static interface GridEngineListener{
        void gridIsProcessed();
    }
}
