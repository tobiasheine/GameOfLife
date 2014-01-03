package com.coderetreat.gol.engine.concurrent;

import com.coderetreat.gol.grid.IGrid;
import com.coderetreat.gol.grid.cell.Cell;

public class GridWorkerDecorator implements GridWorker {
    private final GridWorker gridWorker;

    public GridWorkerDecorator(GridWorker gridWorker) {
        this.gridWorker = gridWorker;
    }

    @Override
    public void processNextGeneration(IGrid grid) {
        gridWorker.processNextGeneration(grid);
    }

    @Override
    public void run() {
        gridWorker.run();
    }

    @Override
    public void addPosition(Cell.Position position) {
        gridWorker.addPosition(position);
    }
}
