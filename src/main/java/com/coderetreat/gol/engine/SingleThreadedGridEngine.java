package com.coderetreat.gol.engine;

import com.coderetreat.gol.engine.grid.IGrid;
import com.coderetreat.gol.engine.rules.IGameOfLifeRules;
import com.coderetreat.gol.models.Cell;

public class SingleThreadedGridEngine extends AbstractGridEngine {
    public SingleThreadedGridEngine(IGameOfLifeRules rules) {
        super(rules);
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

                IGameOfLifeRules.Rules ruleForCell = rules.getRuleForCell(cell);
                cellRulesMap.put(cell,ruleForCell);
            }
        }

        applyRulesToAllCells();
    }
}
