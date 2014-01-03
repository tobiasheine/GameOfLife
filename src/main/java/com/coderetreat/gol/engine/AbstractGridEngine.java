package com.coderetreat.gol.engine;

import com.coderetreat.gol.grid.cell.Cell;
import com.coderetreat.gol.ruleset.IGameOfLifeRuleSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractGridEngine implements IGridEngine{
    protected final IGameOfLifeRuleSet rules;
    protected final Map<Cell, IGameOfLifeRuleSet.Rule> cellRulesMap;
    protected final GridEngineListener engineListener;

    public AbstractGridEngine(IGameOfLifeRuleSet rules, GridEngineListener engineListener) {
        this.rules = rules;
        this.engineListener = engineListener;
        this.cellRulesMap = new ConcurrentHashMap<Cell, IGameOfLifeRuleSet.Rule>();
    }

    protected void applyRulesToAllCells() {
        for (Cell cell : cellRulesMap.keySet()){
            applyRuleForCell(cell,cellRulesMap.get(cell));
        }
    }

    protected void applyRuleForCell(Cell cell, IGameOfLifeRuleSet.Rule rule){
        if(rule.equals(IGameOfLifeRuleSet.Rule.CELL_COMES_ALIVE)){
            cell.setAlive(true);
        }else if (cellDies(rule)){
            cell.setAlive(false);
        }
    }

    protected boolean cellDies(IGameOfLifeRuleSet.Rule rule) {
        return rule.equals(IGameOfLifeRuleSet.Rule.CELL_DIES_BECAUSE_OF_LONELINESS) || rule.equals(IGameOfLifeRuleSet.Rule.CELL_DIES_BECAUSE_OF_OVERPOPULATION);
    }
}
