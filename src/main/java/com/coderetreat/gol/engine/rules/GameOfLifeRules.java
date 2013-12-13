package com.coderetreat.gol.engine.rules;

import com.coderetreat.gol.engine.IGrid;
import com.coderetreat.gol.models.Cell;
import java.util.ArrayList;
import java.util.List;

public class GameOfLifeRules implements IGameOfLifeRules {
    private final IGrid grid;

    public GameOfLifeRules(IGrid grid) {
        this.grid = grid;
    }

    @Override
    public Rules getRuleForCell(Cell cell) {
        List<Cell> neighbours = getCellNeighbours(cell.getPosition());

        int numberAliveNeighbours = getNumAliveNeighbours(neighbours);

        if (!cell.isAlive() && numberAliveNeighbours == 3){
            return Rules.CELL_COMES_ALIVE;
        }

        if(cell.isAlive() && numberAliveNeighbours < 2){
            return Rules.CELL_DIES_BECAUSE_OF_LONELINESS;
        }

        if(cell.isAlive() && numberAliveNeighbours > 1 && numberAliveNeighbours < 4){
            return Rules.CELL_STAYS_ALIVE;

        }

        return Rules.CELL_IS_NOT_TOUCHED;
    }

    private int getNumAliveNeighbours(List<Cell> neighbours) {
        int numberAliveNeighbours = 0;
        for (Cell neighbour : neighbours){
            if (neighbour.isAlive()){
                numberAliveNeighbours++;
            }
        }
        return numberAliveNeighbours;
    }

    private List<Cell> getCellNeighbours(Cell.Position position) {
        List<Cell> neighbours = new ArrayList<Cell>();

        List<Cell.Position> neighbourPositions = getNeighbourPositions(position);
        for (Cell.Position neighbourPosition : neighbourPositions) {
            try {
                neighbours.add(grid.getCellForPosition(neighbourPosition));
            } catch (IllegalArgumentException exception) {
                continue;
            }
        }

        return neighbours;
    }

    private List<Cell.Position> getNeighbourPositions(Cell.Position position) {
        List<Cell.Position> positions = new ArrayList<Cell.Position>();

        int x = position.getX();
        int y = position.getY();

        positions.add(new Cell.Position(x - 1, y + 1));
        positions.add(new Cell.Position(x, y + 1));
        positions.add(new Cell.Position(x + 1, y + 1));
        positions.add(new Cell.Position(x - 1, y));
        positions.add(new Cell.Position(x + 1, y));
        positions.add(new Cell.Position(x - 1, y - 1));
        positions.add(new Cell.Position(x, y - 1));
        positions.add(new Cell.Position(x + 1, y - 1));


        return positions;
    }
}
