package com.coderetreat.gol.engine;

import com.coderetreat.gol.grid.IGrid;
import com.coderetreat.gol.ruleset.IGameOfLifeRuleSet;
import com.coderetreat.gol.grid.cell.Cell;

public class SingleThreadedGridEngine extends AbstractGridEngine {
    public SingleThreadedGridEngine(IGameOfLifeRuleSet rules, GridEngineListener engineListener) {
        super(rules, engineListener);
    }

    @Override
    public void processNextGeneration(IGrid grid) {
        cellRulesMap.clear();

        int width = grid.getWidth();
        int height = grid.getHeight();

        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                Cell.Position positionForNextCell = new Cell.Position(x,y);
                Cell cell = grid.getCellForPosition(positionForNextCell);

                IGameOfLifeRuleSet.Rule ruleForCell = rules.getRuleForCell(cell);
                cellRulesMap.put(cell,ruleForCell);
            }
        }

        applyRulesToAllCells();
        engineListener.gridIsProcessed();
    }
}
