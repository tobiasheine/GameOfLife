package com.coderetreat.gol.ruleset;

import com.coderetreat.gol.grid.IGrid;
import com.coderetreat.gol.grid.cell.Cell;
import java.util.List;

public class GameOfLifeRules implements IGameOfLifeRules {
    private final IGrid grid;

    public GameOfLifeRules(IGrid grid) {
        this.grid = grid;
    }

    @Override
    public Rules getRuleForCell(Cell cell) {
        List<Cell> neighbours = grid.getCellNeighbours(cell.getPosition());
        int numberAliveNeighbours = getNumAliveNeighbours(neighbours);

        if (!cell.isAlive() && numberAliveNeighbours == 3) {
            return Rules.CELL_COMES_ALIVE;
        }

        if (cell.isAlive() && numberAliveNeighbours < 2) {
            return Rules.CELL_DIES_BECAUSE_OF_LONELINESS;
        }

        if (cell.isAlive() && numberAliveNeighbours > 1 && numberAliveNeighbours < 4) {
            return Rules.CELL_STAYS_ALIVE;

        }

        if (cell.isAlive() && numberAliveNeighbours > 3) {
            return Rules.CELL_DIES_BECAUSE_OF_OVERPOPULATION;

        }

        return Rules.CELL_DOES_NOT_CHANGE;
    }

    private int getNumAliveNeighbours(List<Cell> neighbours) {
        int numberAliveNeighbours = 0;
        for (Cell neighbour : neighbours) {
            if (neighbour.isAlive()) {
                numberAliveNeighbours++;
            }
        }
        return numberAliveNeighbours;
    }
}
