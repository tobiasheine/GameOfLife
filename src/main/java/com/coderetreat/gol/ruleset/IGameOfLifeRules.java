package com.coderetreat.gol.ruleset;

import com.coderetreat.gol.grid.cell.Cell;

public interface IGameOfLifeRules {
    public Rules getRuleForCell(Cell cell);

    public enum Rules {
        CELL_DIES_BECAUSE_OF_LONELINESS,
        CELL_DIES_BECAUSE_OF_OVERPOPULATION,
        CELL_COMES_ALIVE,
        CELL_STAYS_ALIVE,
        CELL_DOES_NOT_CHANGE;
    }

}
