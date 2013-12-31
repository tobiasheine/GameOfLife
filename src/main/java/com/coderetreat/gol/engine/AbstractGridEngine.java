package com.coderetreat.gol.engine;

import com.coderetreat.gol.engine.rules.IGameOfLifeRules;
import com.coderetreat.gol.models.Cell;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractGridEngine implements IGridEngine{
    protected final IGameOfLifeRules rules;
    protected final Map<Cell, IGameOfLifeRules.Rules> cellRulesMap;

    public AbstractGridEngine(IGameOfLifeRules rules) {
        this.rules = rules;
        this.cellRulesMap = new HashMap<Cell, IGameOfLifeRules.Rules>();
    }

    protected void applyRulesToAllCells() {
        for (Cell cell : cellRulesMap.keySet()){
            applyRuleForCell(cell,cellRulesMap.get(cell));
        }
    }

    protected void applyRuleForCell(Cell cell, IGameOfLifeRules.Rules rule){
        if(rule.equals(IGameOfLifeRules.Rules.CELL_COMES_ALIVE)){
            cell.setAlive(true);
        }else if (cellDies(rule)){
            cell.setAlive(false);
        }
    }

    protected boolean cellDies(IGameOfLifeRules.Rules rule) {
        return rule.equals(IGameOfLifeRules.Rules.CELL_DIES_BECAUSE_OF_LONELINESS) || rule.equals(IGameOfLifeRules.Rules.CELL_DIES_BECAUSE_OF_OVERPOPULATION);
    }
}
