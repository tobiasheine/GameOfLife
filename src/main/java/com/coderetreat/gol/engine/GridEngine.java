package com.coderetreat.gol.engine;

import com.coderetreat.gol.engine.rules.IGameOfLifeRules;
import com.coderetreat.gol.models.Cell;
import java.util.HashMap;
import java.util.Map;

public class GridEngine implements IGridEngine {

    private final IGameOfLifeRules rules;
    private final Map<Cell, IGameOfLifeRules.Rules> cellRulesMap;

    public GridEngine(IGameOfLifeRules rules) {
        this.rules = rules;
        this.cellRulesMap = new HashMap<Cell, IGameOfLifeRules.Rules>();
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

    private void applyRulesToAllCells() {
        for (Cell cell : cellRulesMap.keySet()){
            applyRuleForCell(cell,cellRulesMap.get(cell));
        }
    }

    private void applyRuleForCell(Cell cell, IGameOfLifeRules.Rules rule){
        if(rule.equals(IGameOfLifeRules.Rules.CELL_COMES_ALIVE)){
            cell.setAlive(true);
        }else if (cellDies(rule)){
            cell.setAlive(false);
        }
    }

    private boolean cellDies(IGameOfLifeRules.Rules rule) {
        return rule.equals(IGameOfLifeRules.Rules.CELL_DIES_BECAUSE_OF_LONELINESS) || rule.equals(IGameOfLifeRules.Rules.CELL_DIES_BECAUSE_OF_OVERPOPULATION);
    }
}
