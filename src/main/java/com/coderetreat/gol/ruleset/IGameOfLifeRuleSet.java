package com.coderetreat.gol.ruleset;

import com.coderetreat.gol.grid.cell.Cell;

public interface IGameOfLifeRuleSet {
    public Rule getRuleForCell(Cell cell);

    public enum Rule {
        CELL_DIES_BECAUSE_OF_LONELINESS,
        CELL_DIES_BECAUSE_OF_OVERPOPULATION,
        CELL_COMES_ALIVE,
        CELL_STAYS_ALIVE,
        CELL_DOES_NOT_CHANGE;
    }

}
