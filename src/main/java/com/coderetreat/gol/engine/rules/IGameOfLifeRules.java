package com.coderetreat.gol.engine.rules;

import com.coderetreat.gol.models.Cell;

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
