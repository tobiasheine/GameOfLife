package com.coderetreat.gol.engine.concurrent;

import com.coderetreat.gol.grid.IGrid;
import com.coderetreat.gol.grid.cell.Cell;
import com.coderetreat.gol.ruleset.IGameOfLifeRules;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseGridWorker implements GridWorker{
    private final IGrid grid;
    private final Map<Cell, IGameOfLifeRules.Rules> cellRulesMap;
    private final IGameOfLifeRules rules;
    private final List<Cell.Position> positionList;

    public BaseGridWorker(IGrid grid, Map<Cell, IGameOfLifeRules.Rules> cellRulesMap, IGameOfLifeRules rules) {
        this.grid = grid;
        this.cellRulesMap = cellRulesMap;
        this.rules = rules;
        this.positionList = new ArrayList<Cell.Position>();
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

            if (ruleForCell != IGameOfLifeRules.Rules.CELL_DOES_NOT_CHANGE){
                cellRulesMap.put(cell, ruleForCell);
            }
        }
    }

    @Override
    public void addPosition(Cell.Position position) {
        positionList.add(position);
    }
}
