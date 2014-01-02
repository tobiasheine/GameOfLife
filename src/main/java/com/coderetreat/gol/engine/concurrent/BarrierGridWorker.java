package com.coderetreat.gol.engine.concurrent;

import com.coderetreat.gol.engine.IGridEngine;
import com.coderetreat.gol.grid.IGrid;
import com.coderetreat.gol.grid.cell.Cell;
import com.coderetreat.gol.ruleset.IGameOfLifeRules;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

class BarrierGridWorker implements Runnable, IGridEngine {

    private final List<Cell.Position> positionList;
    private final IGrid grid;
    private final CyclicBarrier barrier;
    private final Map<Cell, IGameOfLifeRules.Rules> cellRulesMap;
    private final IGameOfLifeRules rules;

    BarrierGridWorker(IGrid grid, CyclicBarrier barrier, Map<Cell, IGameOfLifeRules.Rules> cellRulesMap, IGameOfLifeRules rules) {
        this.barrier = barrier;
        this.cellRulesMap = cellRulesMap;
        this.rules = rules;
        this.positionList = new ArrayList<Cell.Position>();
        this.grid = grid;
    }

    void addPosition(Cell.Position position) {
        positionList.add(position);
    }

    @Override
    public void run() {
        processNextGeneration(grid);
    }

    @Override
    public void processNextGeneration(IGrid grid) {
        for (Cell.Position position : positionList) {
            Cell cell = grid.getCellForPosition(position);

            IGameOfLifeRules.Rules ruleForCell = rules.getRuleForCell(cell);
            cellRulesMap.put(cell, ruleForCell);
        }

        try {
            barrier.await();
        } catch (InterruptedException e) {
            return;
        } catch (BrokenBarrierException e) {
            return;
        }
    }
}
