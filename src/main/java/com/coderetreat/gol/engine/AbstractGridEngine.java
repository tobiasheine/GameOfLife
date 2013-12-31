package com.coderetreat.gol.engine;

import com.coderetreat.gol.grid.cell.Cell;
import com.coderetreat.gol.ruleset.IGameOfLifeRules;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractGridEngine implements IGridEngine{
    protected final IGameOfLifeRules rules;
    protected final Map<Cell, IGameOfLifeRules.Rules> cellRulesMap;
    protected final GridEngineListener engineListener;

    public AbstractGridEngine(IGameOfLifeRules rules, GridEngineListener engineListener) {
        this.rules = rules;
        this.engineListener = engineListener;
        this.cellRulesMap = new ConcurrentHashMap<Cell, IGameOfLifeRules.Rules>();
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
